package ui.view.screen.main.components

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_gallery
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import ui.view.custom.ColoredComposable
import ui.view.screen.main.intents.PermissionDialogIntent
import ui.view.themes.AppColors
import ui.view.themes.AppShapes


@Composable
fun PermissionDialog(
    onIntent: (PermissionDialogIntent) -> Unit
) {
    Dialog(onDismissRequest = { onIntent(PermissionDialogIntent.Deny) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(AppColors.Surface)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppColors.Primary)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_gallery),
                        contentDescription = "Gallery Icon",
                        tint = Color(0xFF26184A),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Access your photos?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val description = buildAnnotatedString {
                    append("Select your photos to use as a visual reference for ")
                    withStyle(style = SpanStyle(color = AppColors.Primary, fontWeight = FontWeight.SemiBold)) {
                        append("Apero")
                    }
                    append(" to generate new art.")
                }

                Text(
                    text = description,
                    color = Color(0xFFA0A0AB),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                ColoredComposable(
                    cornerRadius = 12.dp,
                    colors = listOf(Color.White, Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Button(
                        onClick = { onIntent(PermissionDialogIntent.Grant)},
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryDark,
                            contentColor = AppColors.TextPrimary
                        ),
                        shape = AppShapes.medium
                    ) {
                        Text(
                            text = "Grant Permission",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.SurfaceLight)
                        .border(1.dp, AppColors.TextMuted, AppShapes.medium)
                        .clickable {
                            onIntent(PermissionDialogIntent.Deny)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Not now",
                        color = AppColors.TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
