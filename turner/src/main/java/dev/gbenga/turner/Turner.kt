package dev.gbenga.turner

import android.graphics.Point
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Turner(
    modifier: Modifier = Modifier,
    icons: List<ImageVector>,
    turnerState: TurnerState,
    radius: Dp = 50.dp, //373A40
    controlColor: Color = Color(0xFF222424)
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }

    // TODO: To use with icon click later on
    var turnerRotateDegree by remember {
        mutableFloatStateOf(0f)
    }

    val iconAnim = remember {
        Animatable(0f)
    }

    val iconVectors = icons.map { rememberVectorPainter(image = it)  }

    var iconColor by remember {
        mutableStateOf(Color.Gray)
    }

    var outerCircleState by remember {
        mutableStateOf(true)
    }

    var iconPoint: Offset = Offset.Zero

    val radiusAnimation = remember { Animatable(0f) }
    var smallCircleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var smallCircleRadius by remember {
        mutableFloatStateOf(0f)
    }

    var smallerCircleRadius by remember {
        mutableFloatStateOf(0f)
    }

    val outerCircleRadius = size.width / 2

    val negativePadding = 40
    val iconSpacing by remember {
        derivedStateOf { 360 / iconVectors.size }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(outerCircleState) {

        val animationSpec = tween<Float>(
            durationMillis =100,
            easing = FastOutLinearInEasing
        )

        launch {
            if(outerCircleState){

                iconAnim.animateTo(0f,
                    animationSpec = animationSpec)
                radiusAnimation.animateTo(0f)

            }else{
                radiusAnimation.animateTo(outerCircleRadius)
                iconAnim.animateTo(1f,
                    animationSpec = animationSpec)
            }
        }
    }



    Box(modifier = modifier
        .onGloballyPositioned { layoutCoords ->
            size = layoutCoords.size.let {
                Size(
                    width = it.width.toFloat(),
                    height = it.height.toFloat()
                )
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(onTap = { point ->
                // Calculate the radian of the inner circle
                iconVectors.forEachIndexed { index, vectorPainter ->
                    val degree = (iconSpacing * index).toDouble()
                    val x =
                        (smallCircleCenter.x + (smallCircleRadius
                                * sin(Math.toRadians(degree))) - negativePadding).toFloat()
                    val y =
                        (smallCircleCenter.y + (smallCircleRadius
                                * cos(Math.toRadians(degree))) - negativePadding).toFloat()

                    Log.d("pointerInput", " --> ${point.y} - ${y}")

                }
                //smallCircleCenter
                // Under this

                if ((point.x <= (smallCircleCenter.x + smallerCircleRadius)
                            && point.x > (smallCircleCenter.x - smallerCircleRadius))
                    && (point.y <= (smallCircleCenter.y + smallerCircleRadius)
                            && point.y >= (smallCircleCenter.y - smallerCircleRadius))) {
                    outerCircleState = !outerCircleState
                }
            })

        }
        .drawWithCache {
            //val center =
            onDrawWithContent {

                smallCircleRadius = size.width / 3.5f
                val outerCircleCenter = Offset(x = size.width / 2, y = size.height / 2)
                smallerCircleRadius = size.width / 5
                val innerStrokeRadius = size.width / 6
                smallCircleCenter = Offset(x = size.width / 2, y = size.height / 2)

                // Outer circle
                drawCircle(
                    color = controlColor,
                    radius = radiusAnimation.value,
                    center = outerCircleCenter,
                    //style = Stroke(width = outerCircleRadius)
                )

                drawCircle(
                    color = Color(0xFF131415),
                    radius = smallCircleRadius,
                    center = smallCircleCenter
                ) // Small circle
                drawCircle(
                    color = Color(0xFF131415),
                    radius = smallerCircleRadius,
                    center = smallCircleCenter,
                ) // Smaller circle

                val strokeDiameter = (2 * innerStrokeRadius)
                val pointerWidth = 50f
//                // Strokes
                drawCircle(
                    color = Color.White.copy(alpha = .1f),
                    radius = (innerStrokeRadius * 1.1f),
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    style = Stroke(width = 3f)
                ) // First inner stroke

                // Inner thicker scroll
                drawCircle(
                    color = Color(0xFF0A0A0B),
                    radius = (innerStrokeRadius * 1.20f),
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    style = Stroke(width = 30f)
                ) // First inner stroke

                rotate(degrees = turnerRotateDegree) {
                    // Draw controller inner line
                    drawPath(
                        Path().apply {
                            arcTo(
                                rect = Rect(
                                    Offset(
                                        size.width / 3,
                                        (size.height / 2 - innerStrokeRadius)
                                    ),
                                    Size(strokeDiameter, strokeDiameter)
                                ),
                                startAngleDegrees = 10f,
                                sweepAngleDegrees = 340f,
                                forceMoveTo = false
                            )
                        }, color = Color(0xFF0A0A0B),
                        style = Stroke(width = 5f)
                    )

                    drawRoundRect(
                        color = Color.Green,
                        topLeft = Offset(
                            (size.width / 3 + (strokeDiameter - pointerWidth)),
                            size.height / 2
                        ),
                        size = Size(pointerWidth, 10f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }

                // Very Bad unsscallable code. But I'm too tired to fix it.

                val iconPlaceableRadius = outerCircleRadius - 120
                iconVectors.forEachIndexed { index, icon ->
                    with(icon) {
                        //iconSpacing * index
                        val degree = (iconSpacing * index).toDouble()
                        iconPoint = Offset(
                            x = (center.x + (iconPlaceableRadius * sin(Math.toRadians(degree))) - negativePadding).toFloat(),
                            y = (center.y + (iconPlaceableRadius * cos(Math.toRadians(degree))) - negativePadding).toFloat()
                        )

                        arrange(
                            x = iconPoint.x,
                            y = iconPoint.y
                        ) {
                            draw(
                                icon.intrinsicSize * iconAnim.value,
                                colorFilter = ColorFilter.tint(color = iconColor),
                                // alpha = iconAnim.value
                            )
                        }
                    }
                }
            }
        })
}

fun Float.getX(centerX: Float, angleInDegrees: Float): Float{
    return (centerX + (this * (cos(angleInDegrees) * (PI/ 180)))).toFloat()
}

fun Float.getY(centerY: Float, angleInDegrees: Float): Float{
    return (centerY - (this * sin(angleInDegrees) * (PI/ 180))).toFloat()
}

fun DrawScope.arrange(x: Float, y: Float, block: DrawScope.() -> Unit){
    translate(left = x,
        top = y, block = block)
}


class TurnerState internal constructor() {
    var state by mutableIntStateOf(0)
}

@Composable
fun rememberTurnerState(key: Any? = null): TurnerState {
    return remember(key) {
        TurnerState()
    }
}

/*
(left =(10* 90)/ Math.PI.toFloat(),
                        top = (10* 90)/ Math.PI.toFloat())
 */