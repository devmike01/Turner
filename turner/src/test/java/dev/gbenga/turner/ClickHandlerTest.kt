package dev.gbenga.turner

import androidx.compose.ui.geometry.Offset
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ClickHandlerTest {

    private val clickHandler = ClickHandlerImpl()


    @Before
    fun setUp(){
        hashMapOf(
            1 to ClickBound(start = Offset(10f, 10f),
                end = Offset(80f, 100f)), // selected
            2 to ClickBound(start = Offset(13f, 13f),
                end = Offset(53f, 103f))
        ).forEach { (pos, bound) ->
            clickHandler.setClickBound(pos, bound)
        }
    }


    @Test
    fun `test handle control click`(){
        clickHandler.handleControllerClick(
            point = Offset(70f, 90f),
            smallCircleCenter = Offset(100f, 100f),
            smallerCircleRadius = 39f,
        ){point ->
            assertEquals(point.y, 90f)
            assertEquals(point.x, 70f)
        }
    }

    @Test
    fun `test button count`(){
       assertEquals(2, clickHandler.buttonCount)
    }

    @Test
    fun `test handle button click`(){
        clickHandler.handleIconClick(
            point = Offset(70f, 90f),
            smallCircleCenter = Offset(11f, 12f),
            smallerCircleRadius = 9f,
        ){ position, point ->
            assertEquals(point.y, 90f)
            assertEquals(point.x, 70f)
            assertEquals(1, position)
        }
    }
}