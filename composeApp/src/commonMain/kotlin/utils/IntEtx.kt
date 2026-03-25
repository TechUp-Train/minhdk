package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Float.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }