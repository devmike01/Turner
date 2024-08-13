package dev.gbenga.turner

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.graphics.vector.VectorPainter

data class IconData(val painter: VectorPainter,
                    val circleAnim: Animatable<Float, AnimationVector1D>,
                    val fadeAnim: Animatable<Float, AnimationVector1D>)