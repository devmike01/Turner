package dev.gbenga.turner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset

@Composable
fun rememberClickHandler(): ClickHandler{
    return remember { ClickHandlerImpl() }
}

interface ClickHandler {
    fun handleIconClick(point: Offset, smallCircleCenter: Offset, smallerCircleRadius: Float,
                        onClickPosition: (Int, Offset) -> Unit)
    fun handleControllerClick(point: Offset, smallCircleCenter: Offset, smallerCircleRadius: Float,
                              onClickController: (Offset) -> Unit)

    fun setClickBound(pos: Int, bound: ClickBound)

    val buttonCount : Int
}

class ClickHandlerImpl: ClickHandler{

    private val clickCoordinates = HashMap<Int, ClickBound>()

    override fun handleIconClick(point: Offset, smallCircleCenter: Offset, smallerCircleRadius: Float,
                        onClickPosition: (Int, Offset) -> Unit){
        // Handle item click
        clickCoordinates.forEach { (iconPos, clickPointer) ->

            // Determine the button that was clicked
            if (point.y >= clickPointer.start.y && point.y <= clickPointer.end.y
                && point.x >= clickPointer.start.x && point.x <= clickPointer.end.x
            ) {
                onClickPosition(iconPos, point)

            }

        }

    }




    override fun handleControllerClick(
        point: Offset,
        smallCircleCenter: Offset,
        smallerCircleRadius: Float,
        onClickController: (Offset) -> Unit
    ) {
        if ((point.x <= (smallCircleCenter.x + smallerCircleRadius)
                    && point.x > (smallCircleCenter.x - smallerCircleRadius))
            && (point.y <= (smallCircleCenter.y + smallerCircleRadius)
                    && point.y >= (smallCircleCenter.y - smallerCircleRadius))
        ) {
            onClickController(point)
        }
    }

    override fun setClickBound(pos: Int, bound: ClickBound) {
        clickCoordinates[pos] = bound
    }

    override val buttonCount: Int
        get() = clickCoordinates.size


}