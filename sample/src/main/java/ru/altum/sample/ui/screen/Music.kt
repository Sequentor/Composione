package ru.altum.sample.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import ru.altum.composione.destination.key
import ru.altum.composione.destination.requiredArgs
import ru.altum.sample.R
import ru.altum.sample.core.navigation.AnimatedDestination
import ru.altum.sample.core.navigation.AppRouter
import ru.altum.sample.ui.theme.Color2

@Serializable
internal class Music(val value: String) : AnimatedDestination {
    @Composable
    override fun Content() {
        MusicScreen()
    }
}

@HiltViewModel
internal class MusicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appRouter: AppRouter,
) : ViewModel() {

    private val args = savedStateHandle.requiredArgs<Music>()

    private val _textState = MutableStateFlow(args.value)
    val textState: StateFlow<String> = _textState

    fun onTextChanged(text: String) = _textState.update { text }

    fun onBackToSettings() {
        appRouter.backTo(key<Settings>())
    }

    fun onBackClick() = appRouter.back()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MusicScreen(viewModel: MusicViewModel = hiltViewModel()) {
    val text by viewModel.textState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color2)
    ) {
        TopAppBar(
            title = { Text(text = "MusicScreen") },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent
            ),
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.onBackClick() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left_24),
                        contentDescription = null
                    )
                }
            }
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
            content = { Text("Back to Settings") },
            onClick = { viewModel.onBackToSettings() }
        )
    }
}