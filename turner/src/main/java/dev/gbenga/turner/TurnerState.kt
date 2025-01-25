package dev.gbenga.turner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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