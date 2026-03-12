package com.example.myfirstkmp.migration.session3.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.theme.AppTheme

/**
 * ⭐ BÀI TẬP 1: Interactive Counter (Easy)
 *
 * Yêu cầu:
 * - Text hiển thị count (fontSize 48sp)
 * - Row: Button "−" (disabled khi count = 0) | Button "+" | Button "Reset"
 * - rememberSaveable: xoay màn hình count vẫn giữ
 * - State Hoisting: tách thành Counter (stateless) + CounterScreen (stateful)
 *
 * Tiêu chí:
 * - Compile pass
 * - Xoay màn hình → state giữ nguyên
 * - Counter composable chỉ nhận count + callbacks (không có state bên trong)
 *
 * Gợi ý:
 * - fun Counter(count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit, onReset: () -> Unit)
 * - fun CounterScreen() { var count by rememberSaveable { ... }; Counter(count, ...) }
 */

@Composable
fun BaseButton(
    onClick: () -> Unit,
    size: Dp = 40.dp,
    background: Color = Color.Transparent,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    icon: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(background)
            .border(borderWidth, borderColor, shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
fun ColumnScope.MainComponent(
    modifier: Modifier = Modifier,
    count: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onReset: () -> Unit
) {
    Text(
        text = count.toString(),
        color = Color.Black,
        fontSize = 80.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(top = 25.dp, bottom = 20.dp)
    ) {
        BaseButton(
            background = Color.Transparent,
            borderWidth = 2.dp,
            borderColor = Color.Black,
            onClick = onPlus
        ) {
            Image(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

        BaseButton(
            background = Color.Transparent,
            borderWidth = 2.dp,
            borderColor = Color.Black,
            onClick = onMinus
        ) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }

    BaseButton(
        size = 80.dp,
        onClick = onReset,
        background = Color.Transparent,
        borderWidth = 2.dp,
        borderColor = Color.Black,
    ) {
        Text(
            text = "Reset",
            color = Color.Red
        )
    }
}

@Composable
fun CounterScreen() {

    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = "Interactive Counter",
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        MainComponent(
            count = count,
            onPlus = { count++ },
            onMinus = { count-- },
            onReset = { count = 0 }
        )

    }

}


@Composable
fun InteractiveCounterScreen() {
    // TODO: Xóa placeholder này và gọi CounterScreen() đã implement ở trên
    CounterScreen()
}

@Preview(showBackground = true)
@Composable
private fun InteractiveCounterScreenPreview() {
    AppTheme { InteractiveCounterScreen() }
}
