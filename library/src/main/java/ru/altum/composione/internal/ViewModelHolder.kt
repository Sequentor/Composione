package ru.altum.composione.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

internal class ViewModelHolder : ViewModel() {
    private val viewModelStores = mutableMapOf<String, ViewModelStore>()

    override fun onCleared() {
        viewModelStores.forEach { (_, store) -> store.clear() }
    }

    fun viewModelStoreForDestination(key: String): ViewModelStore =
        viewModelStores.getOrPut(key) { ViewModelStore() }

    fun clearViewModelStoreOwnerForKey(key: String) {
        viewModelStores.remove(key)?.clear()
    }
}

internal fun ViewModelStore.getViewModelHolder(): ViewModelHolder {
    val provider = ViewModelProvider.create(
        store = this,
        factory = viewModelFactory { initializer { ViewModelHolder() } },
    )
    return provider[ViewModelHolder::class]
}