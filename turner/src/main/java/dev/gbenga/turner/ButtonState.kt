package dev.gbenga.turner


sealed interface ButtonState{
    data object Selected : ButtonState
    data object Unselected : ButtonState
    data object Reselected : ButtonState
}