package com.sz.randombark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sz.randombark.common.themes.RandomBarkTheme
import com.sz.randombark.feature.presentation.RandomBarkViewModel
import com.sz.randombark.feature.presentation.RandomDogScreen
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
                    RandomDogScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
