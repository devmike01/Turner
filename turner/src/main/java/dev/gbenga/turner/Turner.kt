package dev.gbenga.turner

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.math.tanh

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


    val turnerRotateDegreeAnim: Float by animateFloatAsState(turnerRotateDegree, label = "turnerRotateDegree")


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

    val iconSpacing by remember {
        derivedStateOf { 360 / iconVectors.size }
    }

    var selectedIconPos by remember { mutableIntStateOf(6) }

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
                launch { iconAnim.animateTo(1f,
                    animationSpec = animationSpec) }
                radiusAnimation.animateTo(outerCircleRadius)
            }
        }
    }

    val clickCoords = HashMap<Int, ClickBound>()
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
                // Log.d("pointerInput", " --> $point")

                // Handle item click
                clickCoords.forEach { (iconPos, clickPointer) ->

                    val degree = (iconSpacing * (iconPos)).toDouble()
                    if (point.y >= clickPointer.start.y && point.y <= clickPointer.end.y
                        && point.x >= clickPointer.start.x && point.x <= clickPointer.end.x
                    ) {
                        selectedIconPos = iconPos
                        /**
                         * Right and left icon is good but not the rest when rotating switch
                         */
                        turnerRotateDegree = (360f / clickCoords.size) * iconPos
                        Log.d("pointerInput", " -> $turnerRotateDegree ${60f * iconPos} $iconPos")

                    }
                }
                // Open controller
                if ((point.x <= (smallCircleCenter.x + smallerCircleRadius)
                            && point.x > (smallCircleCenter.x - smallerCircleRadius))
                    && (point.y <= (smallCircleCenter.y + smallerCircleRadius)
                            && point.y >= (smallCircleCenter.y - smallerCircleRadius))
                ) {
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
                    color = Color.Green.copy(alpha = .1f),
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

                rotate(degrees = turnerRotateDegreeAnim) {
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

                // val iconPlaceableRadius = smallCircleCenter - 270
                iconVectors.forEachIndexed(1) { index, icon ->
                    with(icon) {
                        //iconSpacing * index
                        val degree = (iconSpacing * index).toDouble()

                        //calculate the offset
                        val stepsLabelOffset = Offset(
                            x = center.x + (smallCircleRadius * 1.4f) * cos(
                                degree * (Math.PI / 180)
                            ).toFloat(),
                            y = center.y - (smallCircleRadius * 1.4f) * sin(
                                degree * (Math.PI / 180)
                            ).toFloat()
                        )

                        //subtract the label width and height to position label at the center of the step
                        val stepsLabelTopLeft = Offset(
                            stepsLabelOffset.x - (icon.intrinsicSize.width / 2.3F),
                            stepsLabelOffset.y - (icon.intrinsicSize.height / 2)
                        )

                        arrange(
                            x = stepsLabelTopLeft.x,
                            y = stepsLabelTopLeft.y
                        ) { x, y ->
                            clickCoords[index] = ClickBound(
                                start = Offset(x, y),
                                end = Offset(
                                    x + icon.intrinsicSize.width,
                                    y + icon.intrinsicSize.height
                                )
                            )
                            draw(
                                icon.intrinsicSize * iconAnim.value,
                                colorFilter = ColorFilter.tint(
                                    color = if (selectedIconPos == index) {
                                        Color.Green
                                    } else {
                                        iconColor
                                    }
                                ),
                                // alpha = iconAnim.value
                            )
                        }

                    }
                }
            }
        })
}


fun <T> Iterable<T>.forEachIndexed(start: Int, action: (Int, T) -> Unit){
    this.forEachIndexed{i, data ->
        val index = start + i
        if (index <= this.count()){
            Log.d("MangoTango1", "index: $index")
            action(index, data)
        }else{
            return
        }
    }
}


fun getDegreesFromPoint(x: Double, y: Double): Float {
    // Use atan2 to calculate the angle in radians
    val radians = atan2(y, x)
    // Convert radians to degrees
    var degrees = Math.toDegrees(radians)
    // Ensure the angle is in the range [0, 360]
//    if (degrees < 0) {
//        degrees += 360.0
//    }
    return degrees.toFloat()
}


fun Float.getX(centerX: Float, angleInDegrees: Float): Float{
    return (centerX + (this * (cos(angleInDegrees) * (PI/ 180)))).toFloat()
}

fun Float.getY(centerY: Float, angleInDegrees: Float): Float{
    return (centerY - (this * sin(angleInDegrees) * (PI/ 180))).toFloat()
}

fun DrawScope.arrange(x: Float, y: Float, block: DrawScope.(Float, Float) -> Unit){
    translate(left = x,
        top = y, block = {block(x, y)})
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

data class ClickBound(val start: Offset, val end: Offset)
