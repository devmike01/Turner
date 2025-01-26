package dev.gbenga.turner.presenter

import dev.gbenga.turner.XYOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class TurnerPresenterImpl : TurnerPresenter {

    private val _presenterState = MutableStateFlow(TurnerPresenterState())

    override fun updateSmallCircleCenter(center: XYOffset) {
        _presenterState.update { it.copy(smallCircleCenter = center) }
    }

    override fun updateSmallCircleRadius(radius: Float) {
        _presenterState.update { it.copy(smallCircleRadius = radius) }
    }

    override fun updateSmallerCircleRadius(radius: Float) {
        _presenterState.update { it.copy(smallerCircleRadius = radius) }
    }

    override fun updateSelectedItemPos(position: Int) {
        _presenterState.update { it.copy(selectedItemPos = position) }
    }

    override fun updateTurnerRotateDegree(degree: Float) {
        _presenterState.update { it.copy(turnerRotateDegree = degree) }
    }

    override fun updateEffectAnim(isChecked: Boolean) {
        _presenterState.update { it.copy(clicked = isChecked) }
    }

    override fun setOuterCircleRadius(outerCircleRadius: Float) {
        _presenterState.update {it.copy(outerCircleRadius = outerCircleRadius)}
    }


    override val presenterState: StateFlow<TurnerPresenterState>
        get() = _presenterState

    override fun destroy() {

    }


}