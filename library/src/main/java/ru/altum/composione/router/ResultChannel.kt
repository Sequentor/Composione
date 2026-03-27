package ru.altum.composione.router

fun interface ResultListener<T> {
    fun onResult(data: T)
}

fun interface ResultListenerHandler {
    fun onDispose()
}

internal class ResultChannel {
    private val listeners = mutableMapOf<String, ResultListener<*>>()

    fun <T> setResultListener(key: String, listener: ResultListener<T>): ResultListenerHandler {
        listeners[key] = listener
        return ResultListenerHandler { listeners.remove(key) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> setResult(key: String, data: T) {
        (listeners.remove(key) as? ResultListener<T>)?.onResult(data)
    }
}