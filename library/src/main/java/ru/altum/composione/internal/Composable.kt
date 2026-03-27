package ru.altum.composione.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import ru.altum.composione.destination.Destination

@Composable
internal fun rememberChildViewModelStoreOwner(
    destination: Destination,
    viewModelHolder: ViewModelHolder,
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current
): ViewModelStoreOwner = remember(destination.key) {
    createChildViewModelStoreOwner(
        destination = destination,
        viewModelStore = viewModelHolder.viewModelStoreForDestination(destination.key),
        savedStateRegistryOwner = savedStateRegistryOwner
    )
}

@Composable
internal fun rememberViewModelHolder(): ViewModelHolder {
    val viewModelStore: ViewModelStore = checkNotNull(
        LocalViewModelStoreOwner.current?.viewModelStore
    ) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    return remember { viewModelStore.getViewModelHolder() }
}

