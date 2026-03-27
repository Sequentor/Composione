package ru.altum.sample.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import ru.altum.sample.core.navigation.AnimatedDestination
import ru.altum.sample.core.navigation.AppRouter
import ru.altum.sample.ui.theme.Color1

class HomeResult(val value: String = "HomeResult")

@Serializable
internal class Home : AnimatedDestination {
    @Composable
    override fun Content() {
        HomeScreen()
    }
}

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val appRouter: AppRouter,
) : ViewModel() {

    private val _textState = MutableStateFlow("Hi!")
    val textState: StateFlow<String> = _textState

    val listener = appRouter.setResultListener<HomeResult>("HomeResult") { res ->
        _textState.update { res.value }
    }

    fun onProfileClick() {
        appRouter.navigateTo(Profile(_textState.value))
    }

    fun onTextChanged(text: String) {
        _textState.update { text }
    }

    override fun onCleared() {
        listener.onDispose()
        super.onCleared()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val text by viewModel.textState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color1)
    ) {
        TopAppBar(
            title = { Text(text = "HomeScreen") },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent
            ),
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = text,
            onValueChange = { viewModel.onTextChanged(it) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            content = { Text("Profile") },
            onClick = { viewModel.onProfileClick() }
        )
    }
}