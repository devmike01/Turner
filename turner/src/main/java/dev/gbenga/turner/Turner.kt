package dev.gbenga.turner

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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

    var turnerRotateDegree by remember {
        mutableFloatStateOf(0f)
    }

    val iconsToDraw = icons.mapIndexed { index, imageVector ->
        rememberVectorPainter(image = imageVector)
    }

    var iconColor by remember {
        mutableStateOf(Color.Gray)
    }

    Box(modifier = modifier
        .onGloballyPositioned { layoutCoords ->
            size = layoutCoords.size.let {
                Size(
                    width = it.width.toFloat(),
                    height = it.height.toFloat()
                )
            }
        }.pointerInput(Unit){
            detectTapGestures {
                turnerRotateDegree += 40
                Log.d("detectTapGestures", "${it.x}")
            }

        }
        .drawWithCache {
            //val center =
            onDrawWithContent {

                var outerCircleRadius = size.width / 2
                val smallCircleRadius = size.width / 3.5f

                drawCircle(
                    color = controlColor,
                    radius = outerCircleRadius,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    //style = Stroke(width = outerCircleRadius)
                )

                iconsToDraw.forEach { pairOfIcons ->
                    with(pairOfIcons) {
                        translate(
                            left = (size.width / 5) + (outerCircleRadius / 2),
                            top = size.height / 2
                        ) {
                            draw(pairOfIcons.intrinsicSize)
                        }
                    }
                }

                drawCircle(
                    color = Color(0xFF131415),
                    radius = smallCircleRadius,
                    center = Offset(x = size.width / 2, y = size.height / 2)
                ) // Small circle
                drawCircle(
                    color = Color(0xFF131415),
                    radius = size.width / 5,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                ) // Smaller circle

                val innerStrokeRadius = size.width / 6
                val strokeDiameter = (2 * innerStrokeRadius)
                val pointerWidth = 50f
                // Strokes
                drawCircle(
                    color = Color.White.copy(alpha = .1f),
                    radius = (innerStrokeRadius * 1.1f),
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    style = Stroke(width = 3f)
                ) // First inner stroke

                // Outer thicker scroll
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

                val placeableStartY = outerCircleRadius + 300

                with(iconsToDraw[0]) {
                    arrange(
                        x = outerCircleRadius - (iconsToDraw[0].intrinsicSize.width / 2),
                        y = placeableStartY - (innerStrokeRadius - (iconsToDraw[0].intrinsicSize.height / 2))
                    ) {
                        draw(iconsToDraw[0].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor))
                    }
                }

                with(iconsToDraw[1]) {
                    arrange(
                        x = outerCircleRadius - (iconsToDraw[1].intrinsicSize.width / 2),
                        y = placeableStartY * 1.82f
                    ) {
                        draw(iconsToDraw[1].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor))
                    }
                }

                with(iconsToDraw[2]) {
                    // Left icon
                    arrange(
                        x = outerCircleRadius - (outerCircleRadius / 1.5f),
                        y = placeableStartY
                    ) {
                        draw(iconsToDraw[2].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor))
                    }

                }

                with(iconsToDraw[3]) {
                    arrange(
                        x = outerCircleRadius - (outerCircleRadius / 1.4f),
                        y = placeableStartY + 500
                    ) {
                        draw(iconsToDraw[3].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor))
                    }
                }

                with(iconsToDraw[4]) {
                    // Right
                    arrange(
                        x = outerCircleRadius + (outerCircleRadius / 1.7f),
                        y = placeableStartY
                    ) {
                        draw(iconsToDraw[4].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor))
                    }

                }

                with(iconsToDraw[5]) {

                    arrange(
                        x = outerCircleRadius + (outerCircleRadius / 1.7f),
                        y = placeableStartY + 500
                    ) {
                        draw(
                            iconsToDraw[5].intrinsicSize,
                            colorFilter = ColorFilter.tint(color = iconColor)
                        )
                    }
                }

            }
        })
}
//- (outerCircleRadius - pairOfIcons.second.intrinsicSize.width)


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