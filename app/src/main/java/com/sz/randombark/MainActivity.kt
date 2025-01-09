package com.sz.randombark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sz.randombark.common.themes.RandomBarkTheme
import com.sz.randombark.feature.presentation.RandomBarkViewModel
import com.sz.randombark.feature.presentation.RandomDogUIModel
import com.sz.randombark.feature.presentation.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: RandomBarkViewModel by viewModels()
            RandomBarkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: RandomBarkViewModel
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    when (uiState) {
        is ViewState.Loading -> {
            CircularProgressIndicator(modifier = modifier)
        }

        is ViewState.Error -> {
            Text(
                modifier = modifier,
                text = (uiState as ViewState.Error).message
            )
        }

        is ViewState.Success -> {
            Text(
                modifier = modifier,
                text = (uiState as ViewState.Success<RandomDogUIModel>).data.imageUrl
            )
        }
    }
}