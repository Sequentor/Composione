package ru.altum.composione.destination

import android.os.Bundle
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import ru.altum.composione.internal.decodeDestination
import ru.altum.composione.internal.encodeDestination


interface Destination {
    val key: String get() = this::class.java.name

    fun enterTransition(): EnterTransition = EnterTransition.None
    fun exitTransition(): ExitTransition = ExitTransition.None
    fun popEnterTransition(): EnterTransition = EnterTransition.None
    fun popExitTransition(): ExitTransition = ExitTransition.None
    fun sizeTransform(): SizeTransform = SizeTransform(false)

    @Composable
    fun Content()
}

inline fun <reified T : Destination> key(): String = T::class.java.name

inline fun <reified T : Destination> SavedStateHandle.requiredArgs(key: String = key<T>()): T {
    val destination: Destination? = get<String>(key)?.let { json -> decodeDestination(json) }
    return requireNotNull(destination as? T)
}

inline fun <reified T : Destination> SavedStateHandle.optionalArgs(key: String = key<T>()): T? {
    val destination: Destination? = get<String>(key)?.let { json -> decodeDestination(json) }
    return destination as? T
}

internal fun Destination.toBundle() = Bundle().apply {
    putString(key, encodeDestination(this@toBundle))
}