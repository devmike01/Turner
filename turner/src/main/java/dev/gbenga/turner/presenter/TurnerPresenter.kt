package dev.gbenga.turner.presenter

import dev.gbenga.turner.XYOffset
import kotlinx.coroutines.flow.StateFlow

interface TurnerPresenter {
    fun updateSmallCircleCenter(center: XYOffset)
    fun updateSmallCircleRadius(radius: Float)
    fun updateSmallerCircleRadius(radius: Float)
    fun updateSelectedItemPos(position: Int)
    fun updateTurnerRotateDegree(degree: Float)
    fun updateEffectAnim(isChecked: Boolean)
    fun setOuterCircleRadius(outerCircleRadius: Float)
    val presenterState: StateFlow<TurnerPresenterState>
    fun destroy()
}

