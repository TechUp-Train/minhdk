package ui.view.themes

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.inter_bold
import aigenerator.composeapp.generated.resources.inter_medium
import aigenerator.composeapp.generated.resources.inter_normal
import aigenerator.composeapp.generated.resources.inter_semibold
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font

@Composable
fun interFontFamily() = FontFamily(
    Font(Res.font.inter_normal, weight = FontWeight.Normal),
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.inter_bold, weight = FontWeight.Bold),
)