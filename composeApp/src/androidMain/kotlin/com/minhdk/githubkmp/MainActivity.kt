package com.minhdk.githubkmp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.github.GithubService
import com.minhdk.githubkmp.data.repository.user.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        lifecycleScope.launch {
//            userRepository.getUser("KhacMinh2305").collect { response ->
//                when (response) {
//                    is Response.Success -> {
//                        Log.d("Github_API", "Success - User: ${response.data}")
//                    }
//
//                    is Response.Error -> {
//                        Log.d("Github_API", "Error: ${response.message}")
//                    }
//                }
//            }
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