package com.sz.randombark.feature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun RandomDogScreen(
    modifier: Modifier = Modifier,
    viewModel: RandomBarkViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.viewState.collectAsStateWithLifecycle().value.let { state ->
            when (state) {
                is ViewState.Loading -> {
                    CircularProgressIndicator(modifier = modifier)
                }

                is ViewState.Error -> {
                    Text(text = state.message)
                }

                is ViewState.Success -> {
                    AsyncImage(
                        model = state.data.imageUrl,
                        contentDescription = "Random dog image"
                    )
                    Button(
                        modifier = Modifier.padding(vertical = 16.dp),
                        onClick = { viewModel.fetchRandomDog() },
                        content = { Text(text = "Next") }
                    )
                }
            }
        }
    }
}