package dev.gbenga.turner

import androidx.compose.ui.geometry.Offset

data class XYOffset(val x: Float=0f, val y: Float=0f){
    companion object{
        val zero = XYOffset()
    }

    fun toOffset(): Offset{
        return Offset(x, y)
    }
}