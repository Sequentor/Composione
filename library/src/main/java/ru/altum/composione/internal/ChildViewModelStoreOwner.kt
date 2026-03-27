package ru.altum.composione.internal

import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.serialization.InternalSerializationApi
import ru.altum.composione.destination.Destination
import ru.altum.composione.destination.toBundle

@OptIn(InternalSerializationApi::class)
internal fun createChildViewModelStoreOwner(
    destination: Destination,
    viewModelStore: ViewModelStore,
    savedStateRegistryOwner: SavedStateRegistryOwner
): ViewModelStoreOwner = object : ViewModelStoreOwner,
    SavedStateRegistryOwner by savedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    override val viewModelStore: ViewModelStore
        get() = viewModelStore

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = SavedStateViewModelFactory()

    override val defaultViewModelCreationExtras: CreationExtras
        get() = MutableCreationExtras().also {
            it[SAVED_STATE_REGISTRY_OWNER_KEY] = this
            it[VIEW_MODEL_STORE_OWNER_KEY] = this
            it[DEFAULT_ARGS_KEY] = destination.toBundle()
        }

    init {
        enableSavedStateHandles()
    }
}