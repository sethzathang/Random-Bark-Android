package com.sz.randombark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sz.randombark.common.themes.RandomBarkTheme
import com.sz.randombark.feature.presentation.RandomBarkViewModel
import com.sz.randombark.feature.presentation.RandomDogScreenCoroutine
import com.sz.randombark.feature.presentation.RandomDogScreenRxJava
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAB_COROUTINE = 0
        const val TAB_RXJAVA = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: RandomBarkViewModel by viewModels()
            RandomBarkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowMainContent(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    @Composable
    fun ShowMainContent(
        modifier: Modifier,
        viewModel: RandomBarkViewModel
    ) {
        val selectedTabIndex = rememberSaveable { mutableIntStateOf(value = TAB_COROUTINE) }
        Column(modifier = modifier) {
            // set up Tabs
            TabRow(selectedTabIndex = selectedTabIndex.intValue) {
                Tab(
                    text = { Text(text = "Coroutine") },
                    selected = selectedTabIndex.intValue == TAB_COROUTINE,
                    onClick = { selectedTabIndex.intValue = TAB_COROUTINE }
                )
                Tab(
                    text = { Text(text = "RxJava") },
                    selected = selectedTabIndex.intValue == TAB_RXJAVA,
                    onClick = { selectedTabIndex.intValue = TAB_RXJAVA }
                )
            }

            // scrollable content, can also use LazyColumn
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (selectedTabIndex.intValue) {
                    TAB_COROUTINE -> {
                        LaunchedEffect(Unit) { viewModel.fetchRandomDogCoroutine() }
                        RandomDogScreenCoroutine(viewModel = viewModel)
                    }

                    TAB_RXJAVA -> {
                        LaunchedEffect(Unit) { viewModel.fetchRandomDogRxJava() }
                        RandomDogScreenRxJava(viewModel = viewModel)
                    }
                }
            }
        }
    }
}