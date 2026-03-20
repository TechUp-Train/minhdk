package com.minhdk.githubkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.minhdk.githubkmp.data.core.service.github.GithubUserService
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val gService: GithubUserService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        lifecycleScope.launch {
//            when(val res = gService.fetchUser("minhd2k3")) {
//                is data.network.Response.Success -> {
//                    Log.d("Github_API", "Success: ${res.data}")
//                }
//                is data.network.Response.Error -> {
//                    Log.d("Github_API", "Error: ${res.message}")
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