package dev.gbenga.turner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

class TurnerState internal constructor() {
    private val _selectedPosition = mutableIntStateOf(0)

    @Composable
    fun rememberSelectedPosition() : State<Int>{
        return remember { _selectedPosition }
    }

    fun set(value: Int){
        _selectedPosition.intValue = value
    }
}


@Composable
fun rememberTurnerState(key: Any? = null): TurnerState {
    return remember(key) {
        TurnerState()
    }
}