package com.example.kmpday3.exercises.exercise3

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kmpday3.getImage
import kmpday3.composeapp.generated.resources.Res
import kmpday3.composeapp.generated.resources.image_placeholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun UploadImageScreen() {

    var imageData by remember{ mutableStateOf<ByteArray?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        val modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(50.dp)

        imageData?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = modifier
            )
        } ?: run {
            Image(
                painter = painterResource(Res.drawable.image_placeholder),
                contentDescription = null,
                modifier = modifier
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.width(100.dp).height(80.dp),
            onClick = {
                if(imageData == null) {
                    scope.launch {
                        imageData = getImage()
                        println("imageData: ${imageData != null}")
                    }
                } else {
                    print("push data to server")
                }
            }
        ) {
            Text(if(imageData != null) "Push image" else "Pick image")
        }
    }
}