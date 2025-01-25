package dev.gbenga.turner.presenter

import dev.gbenga.turner.XYOffset

data class TurnerPresenterState(val smallCircleCenter : XYOffset = XYOffset.zero,
                                val smallCircleRadius: Float = 0f,
                                val smallerCircleRadius: Float = 0f,
                                val selectedItemPos : Int = 6,
                                val turnerRotateDegree: Float =0f,
                                val clicked: Boolean = false,
                                val outerCircleRadius: Float = 300f)


