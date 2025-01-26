package dev.gbenga.turner

import dev.gbenga.turner.presenter.TurnerPresenter
import dev.gbenga.turner.presenter.TurnerPresenterImpl
import dev.gbenga.turner.presenter.TurnerPresenterState
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class TurnerPresenterTest {

    private val turnerPresenter: TurnerPresenter = TurnerPresenterImpl()

    private lateinit var presenterState: TurnerPresenterState

    @Before
    fun setUp(){
       // MockKAnnotations.init(this)
    }

    private suspend fun getPresenterState() = turnerPresenter.presenterState.first()

    @Test
    fun `test update small circle center`() {
        runBlocking {
            // Before update
            val presenterStateBefore = getPresenterState()
            assertEquals(0F, presenterStateBefore.smallCircleCenter.x)
            assertEquals(0F, presenterStateBefore.smallCircleCenter.y)

            // After update
            turnerPresenter.updateSmallCircleCenter(XYOffset(4F, 6F))
            val presenterStateAfter = turnerPresenter.presenterState.first()
            assertEquals(4F, presenterStateAfter.smallCircleCenter.x)
            assertEquals(6F, presenterStateAfter.smallCircleCenter.y)
        }
    }

    @Test
    fun `test small circle radius`(){
        runBlocking {
            // Before update
            val presenterStateBefore = getPresenterState()
            assertEquals(0F, presenterStateBefore.smallCircleRadius)

            // After update
            turnerPresenter.updateSmallCircleRadius(10f)
            val presenterStateAfter = getPresenterState()
            assertEquals(10f, presenterStateAfter.smallCircleRadius)
        }
    }


    @Test
    fun `test smaller circle radius`(){
        runBlocking {
            // Before update
            val presenterStateBefore = getPresenterState()
            assertEquals(0F, presenterStateBefore.smallerCircleRadius)

            // After update
            turnerPresenter.updateSmallerCircleRadius(3f)
            val presenterStateAfter = getPresenterState()
            assertEquals(3f, presenterStateAfter.smallerCircleRadius)
        }
    }



    @Test
    fun `test update selected item position`(){
        runBlocking {
            val presenterStateBefore = getPresenterState()
            assertEquals(6, presenterStateBefore.selectedItemPos)

            // After update
            turnerPresenter.updateSelectedItemPos(3)
            val presenterStateAfter = getPresenterState()
            assertEquals(3, presenterStateAfter.selectedItemPos)
        }
    }

    @Test
    fun `test update turner rotate degree`(){
        runBlocking {
            val presenterStateBefore = getPresenterState()
            assertEquals(0F, presenterStateBefore.turnerRotateDegree)

            // After update
            turnerPresenter.updateTurnerRotateDegree(180f)
            val presenterStateAfter = getPresenterState()
            assertEquals(180f, presenterStateAfter.turnerRotateDegree)
        }
    }

    @Test
    fun `test update select button effect`(){
        runBlocking {
            val presenterStateBefore = getPresenterState()
            assertEquals(false, presenterStateBefore.clicked)

            // After update
            turnerPresenter.updateEffectAnim(true)
            val presenterStateAfter = getPresenterState()
            assertEquals(true, presenterStateAfter.clicked)
        }
    }

    @Test
    fun `test update outer circle radius`(){
        runBlocking {
            val presenterStateBefore = getPresenterState()
            assertEquals(300f, presenterStateBefore.outerCircleRadius)

            // After update
            turnerPresenter.setOuterCircleRadius(80f)
            val presenterStateAfter = getPresenterState()
            assertEquals(80f, presenterStateAfter.outerCircleRadius)
        }
    }
}