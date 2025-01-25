package dev.gbenga.turner

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import dev.gbenga.turner.presenter.TurnerPresenter
import dev.gbenga.turner.presenter.TurnerPresenterState
import dev.gbenga.turner.presenter.rememberTurnerPresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
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
    radius: Float = 500f, //373A40
    controlColor: Color = Color(0xFF222424),
    selectedColor: Color = Color.Green,
    unselectColor: Color = Color.LightGray.copy(alpha = .1f)
) {
    val presenter = rememberTurnerPresenter()

    val turnerPresenterState by presenter.presenterState.collectAsState()

    if (icons.size > 6){
        throw IconGreaterThan6Exception()
    }

    var turnerSize by remember { mutableStateOf(Size.Zero) }

    // TODO: To use with icon click later on


    val turnerRotateDegreeAnim: Float by animateFloatAsState(
        turnerPresenterState.turnerRotateDegree,
        label = "turnerRotateDegree")

    val iconAnim = remember { Animatable(0f) }


    var outerCircleState by remember { mutableStateOf(true) }

    val radiusAnimation = remember { Animatable(radius) }

    presenter.setOuterCircleRadius(radius)

    val iconVectors = icons.map { rememberVectorPainter(image = it)  }
    val iconSpacing by remember { derivedStateOf { 360 / iconVectors.size } }

    val clickHandler = rememberClickHandler()

    val coroutine = rememberCoroutineScope()

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

                radiusAnimation.animateTo(turnerPresenterState.outerCircleRadius)
            }
        }
    }

    Box(modifier = modifier
        .onGloballyPositioned { layoutCoordinate ->
            turnerSize = layoutCoordinate.size.let {
                Size(
                    width = it.width.toFloat(),
                    height = it.height.toFloat()
                )
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(onTap = { point ->

                // Handle item click
                clickHandler.handleIconClick(
                    point, turnerPresenterState.smallCircleCenter.toOffset(),
                    turnerPresenterState.smallerCircleRadius
                ) { iconPos, _ ->
                    if (turnerPresenterState.selectedItemPos == iconPos) {
                        return@handleIconClick
                    }

                    //selectedIconPos = iconPos
                    presenter.updateSelectedItemPos(iconPos)

                    /*
                     * Right and left icon is good but not the rest when rotating switch
                     */
                    presenter.updateTurnerRotateDegree(-((360f / clickHandler.buttonCount) * iconPos))
                    turnerState.set(iconPos)

                    coroutine.launch {
                        presenter.updateEffectAnim(true)
                        delay(500)
                        presenter.updateEffectAnim(false)

                    }
                }

                clickHandler.handleControllerClick(
                    point,
                    turnerPresenterState.smallCircleCenter.toOffset(),
                    turnerPresenterState.smallCircleRadius
                ) {
                    outerCircleState = !outerCircleState
                }

            })

        }


        .drawWithCache {
            //val center =
            onDrawWithContent {
                drawAlongSideContent(presenter,
                    turnerPresenterState, controlColor,
                    iconSpacing, radiusAnimation, turnerRotateDegreeAnim,
                    selectedColor, iconAnim, clickHandler, unselectColor, iconVectors)
            }
        })
}


fun ContentDrawScope.drawTurnerCircleLayers(
    turnerRotateDegreeAnim: Float,
    presenter: TurnerPresenter, turnerPresenterState : TurnerPresenterState,
    radiusAnimation: Animatable<Float, AnimationVector1D>, controlColor: Color,){
    val outerCircleCenter = Offset(x = size.width / 2, y = size.height / 2)
    // smallerCircleRadius = 30f // size.width / 5
    val innerStrokeRadius = turnerPresenterState.smallCircleRadius
    presenter.updateSmallCircleCenter(XYOffset(x = size.width / 2, y = size.height / 2))
    // smallCircleCenter = Offset(x = size.width / 2, y = size.height / 2)

    // Outer circle
    drawCircle(
        color = controlColor,
        radius = radiusAnimation.value,
        center = outerCircleCenter,
    )

    drawCircle(
        color = Color(0xFF131415),
        radius = turnerPresenterState.smallCircleRadius,
        center = turnerPresenterState.smallCircleCenter.toOffset()
    )

    // Small circle
    drawCircle(
        color = Color(0xFF131415),
        radius = turnerPresenterState.outerCircleRadius / 2,
        center = turnerPresenterState.smallCircleCenter.toOffset(),
    ) // Smaller circle

    val strokeDiameter = (turnerPresenterState.outerCircleRadius / 1.7f)
    val pointerWidth = 50f
//                // Strokes

    drawControlWithPointer(turnerRotateDegreeAnim, strokeDiameter, pointerWidth)
    drawCircle(
        color = Color.Green.copy(alpha = .1f),
        radius = (innerStrokeRadius * 1.2f),
        center = Offset(x = size.width / 2, y = size.height / 2),
        style = Stroke(width = 3f)
    ) // First inner stroke

    // Inner thicker scroll
    drawCircle(
        color = Color(0xFF0A0A0B),
        radius = (innerStrokeRadius * 1.05f),
        center = Offset(x = size.width / 2, y = size.height / 2),
        style = Stroke(width = 30f)
    ) // First inner stroke

}

fun ContentDrawScope.drawAlongSideContent(presenter: TurnerPresenter,
                                     turnerPresenterState : TurnerPresenterState,
                                     controlColor: Color,
                                     iconSpacing: Int,
                                     radiusAnimation: Animatable<Float, AnimationVector1D>,
                                     turnerRotateDegreeAnim: Float,
                                     selectedColor: Color,
                                     iconAnim: Animatable<Float, AnimationVector1D>,
                                     clickHandler: ClickHandler,
                                     unselectColor: Color,
                                     iconVectors: List<VectorPainter>){

    presenter.updateSmallCircleRadius(turnerPresenterState.outerCircleRadius / 3)

    // Very Bad unsscallable code. But I'm too tired to fix it.

    drawTurnerCircleLayers(turnerRotateDegreeAnim, presenter, turnerPresenterState,
        radiusAnimation, controlColor)

    // val iconPlaceableRadius = smallCircleCenter - 270
    iconVectors.forEachIndexed(1) { index, icon ->
        with(icon) {
            //iconSpacing * index
            val degree = (iconSpacing * index).toDouble()

            //calculate the offset
            val stepsLabelOffset = Offset(
                x = center.x + (turnerPresenterState.outerCircleRadius / 1.3f) * cos(
                    degree * (java.lang.Math.PI / 180)
                ).toFloat(),
                y = center.y - (turnerPresenterState.outerCircleRadius / 1.3f) * sin(
                    degree * (java.lang.Math.PI / 180)
                ).toFloat()
            )

            //subtract the label width and height to position label at the center of the step
            val stepsLabelTopLeft = Offset(
                stepsLabelOffset.x - (icon.intrinsicSize.width / 2),
                stepsLabelOffset.y - (icon.intrinsicSize.height / 2)
            )

            arrange(
                x = stepsLabelOffset.x,
                y = stepsLabelOffset.y
            ) { _, _ ->
                val brush = androidx.compose.ui.graphics.Brush.sweepGradient(
                    listOf(if(turnerPresenterState.clicked
                    && turnerPresenterState.selectedItemPos == index){
                    Color.Cyan.copy(alpha = .2f)
                } else{
                    Color.Transparent
                }, Color.Transparent)
                )

                drawCircle(brush = brush,
                    radius = icon.intrinsicSize.height / 1.5f, center = Offset(
                        0f,
                        0f
                    )
                )
            }

            arrange(
                x = stepsLabelTopLeft.x,
                y = stepsLabelTopLeft.y
            ) { x, y ->
                clickHandler.setClickBound(
                    index, ClickBound(
                        start = Offset(x, y),
                        end = Offset(
                            x + icon.intrinsicSize.width,
                            y + icon.intrinsicSize.height
                        )
                    )
                )
                draw(
                    icon.intrinsicSize * iconAnim.value,
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        color = if (turnerPresenterState.selectedItemPos == index) {
                            selectedColor
                        } else {
                            unselectColor
                        }
                    ),
                    // alpha = iconAnim.value
                )
            }

        }
    }
}

fun DrawScope.drawControlWithPointer(turnerRotateDegreeAnim: Float,
                                     strokeDiameter: Float,
                                     pointerWidth: Float){

    rotate(degrees = turnerRotateDegreeAnim) {

        drawPath(
            Path().apply {
                arcTo(
                    rect = Rect(
                        Offset(
                            (size.width / 2) - (strokeDiameter / 2),
                            (size.height / 2) - (strokeDiameter / 2)
                        ),
                        Size(strokeDiameter, strokeDiameter)
                    ),
                    startAngleDegrees = 10f,
                    sweepAngleDegrees = 342f,
                    forceMoveTo = false
                )
            }, color = Color(0xFF0A0A0B),
            style = Stroke(width = 5f)
        )

        drawRoundRect(
            color = Color.Green,
            topLeft = Offset(
                size.width / 1.75f,
                size.height / 2
            ),
            size = Size(pointerWidth, 10f),
            cornerRadius = CornerRadius(10f, 10f)
        )
    }
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




data class ClickBound(val start: Offset, val end: Offset)
