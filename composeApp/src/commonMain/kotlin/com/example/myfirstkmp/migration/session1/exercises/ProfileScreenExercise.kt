package com.example.myfirstkmp.migration.session1.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.theme.AppTheme
import myfirstkmp.composeapp.generated.resources.Res
import myfirstkmp.composeapp.generated.resources.anhnen
import org.jetbrains.compose.resources.painterResource

/**
 * ⭐⭐⭐ BÀI TẬP 3: Mini Profile Screen (Challenge)
 *
 * Yêu cầu:
 * - Header: Avatar + Name + "Edit Profile" button
 * - Stats Row: 3 cột (Posts | Followers | Following) dùng Row + weight
 * - Bio section: Text nhiều dòng
 * - Action buttons: "Message" + "Follow" ngang hàng
 * - Bonus: Thêm Spacer hợp lý, đúng alignment
 */

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        Image(
            painter = painterResource(Res.drawable.anhnen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Text(
            text = "Doan Khac Minh",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterVertically)
        )

        Button(
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = OceanBlue),
            onClick = {},
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Edit profile",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Stats(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {

        listOf(
            Pair("Post", "128"),
            Pair("Followers", "1.2k"),
            Pair("Following", "380")
        ).forEach { (prop, value) ->
            StatBox(
                prop, value,
                Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .background(
                        color = DeepSeaBlue.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun Buttons(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(25.dp)

//    Button(
//        colors = ButtonDefaults.buttonColors()
//            .copy(containerColor = if (isFollowed) Color.LightGray else OceanBlue),
//        onClick = {
//            if (!isFollowed) isFollowed = true
//        },
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "Follow",
//            color = if (!isFollowed) Color.White else Color.Black
//                .copy(alpha = 0.5f)
//        )
//    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {

        Button(
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = Color.Transparent),
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .border(width = 2.dp, color = OceanBlue, shape = shape)
        ) {
            Text(
                text = "Follow",
                color = OceanBlue,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = Color.Transparent),
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .background(color = OceanBlue, shape = shape)
        ) {
            Text(
                text = "Follow",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Stats(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Android Developer Lover of clean code ✨\nHà Nội, Việt Nam \uD83C\uDDFB\uD83C\uDDF3",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 5.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Buttons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

    }
}

@Composable
fun StatBox(
    property: String,
    stat: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = property,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 5.dp)
        )

        Text(
            text = stat,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AppTheme { ProfileScreen() }
}
