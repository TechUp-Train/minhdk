package com.example.myfirstkmp.migration.session1.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.theme.AppTheme
import myfirstkmp.composeapp.generated.resources.Res
import myfirstkmp.composeapp.generated.resources.anhnen
import org.jetbrains.compose.resources.painterResource

/**
 * ⭐⭐ BÀI TẬP 2: Contact Card (Medium)
 *
 * Yêu cầu:
 * - Row chứa: Image (CircleShape, 60dp) + Column (Name bold + Bio regular)
 * - Button "Follow" ở dưới, fillMaxWidth
 * - Card có elevation, rounded corner 12dp
 * - Modifier chain ít nhất 3 modifier
 * - Dùng cả Column, Row, Box
 * - @Preview với showBackground = true
 */


@Composable
fun ContactCard(
    name: String = "Doan Khac Minh",
    bio: String = "Android Developer tại Apero"
) {

    var isFollowed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Image(
                    painter = painterResource(Res.drawable.anhnen),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(start = 12.dp)
                ) {

                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )

                    Text(
                        text = bio,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )

                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = if (isFollowed) Color.LightGray else OceanBlue),
                onClick = {
                    if (!isFollowed) isFollowed = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Follow",
                    color = if (!isFollowed) Color.White else Color.Black
                        .copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactCardPreview() {
    AppTheme { ContactCard() }
}
