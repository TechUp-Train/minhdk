package com.example.kmpday4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import data.model.UserDto
import data.network.Response
import data.service.github.GithubUserService
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val gService: GithubUserService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            val response = gService.fetchAllUsers()
            Log.d("fbewjkf", "onCreate: ${(response as? Response.Success)?.data}")
        }

        //        request(
//            method = HttpMethod.Get
//        ) {
//            attachDomain("config/{benjamin}/your/{doan}/path", "for", "sub")
//            addHeaders(mapOf("key" to "value"))
//            <other setups>
//        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}