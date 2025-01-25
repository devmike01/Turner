package dev.gbenga.turner.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberTurnerPresenter(): TurnerPresenter{
    return remember { TurnerPresenterImpl() }
}
