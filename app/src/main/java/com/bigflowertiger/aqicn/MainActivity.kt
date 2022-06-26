package com.bigflowertiger.aqicn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bigflowertiger.aqicn.model.AqicnResponse
import com.bigflowertiger.aqicn.network.AQICN_BASE_URL
import com.bigflowertiger.aqicn.network.AqicnService
import com.bigflowertiger.aqicn.ui.theme.AqiCNTheme
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AqicnViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.aqiState.value
            AqiCNTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Button(onClick = {
                                viewModel.fetchAqi("120.173580", "31.384260")
                            }) {
                                Text(text = "Click")
                            }
                            if (state.errMsg.isEmpty()) {
                                Text(text = state.data.toString())
                            } else {
                                Text(text = state.errMsg)
                            }
                        }
                    }
                }
            }
        }
    }
}