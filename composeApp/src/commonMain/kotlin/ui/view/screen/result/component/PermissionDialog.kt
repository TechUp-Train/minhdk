package ui.view.screen.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.koin.dsl.module
import ui.view.custom.ColoredComposable
import ui.view.themes.AppColors

val needPermisison = "Please go to setting then eneble access image permisison, so your image can be downloaded"
val downloadSuccess = "Image downloaded !"

val downloadFailed = "DSownload image failed !"

@Composable
fun NotifyDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

        ColoredComposable(
            colors = listOf(
                AppColors.PrimaryLight,
                Color.Transparent
            ),
            modifier = Modifier
                .width(250.dp)
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(color = AppColors.SurfaceLight, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = AppColors.TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}