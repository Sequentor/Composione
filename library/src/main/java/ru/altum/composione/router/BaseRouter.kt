package ru.altum.composione.router

import ru.altum.composione.command.Command
import ru.altum.composione.command.CommandBuffer

abstract class BaseRouter {
    internal val commandBuffer = CommandBuffer()
    private val resultChannel = ResultChannel()

    fun <T> setResultListener(
        key: String,
        listener: ResultListener<T>
    ): ResultListenerHandler = resultChannel.setResultListener(key, listener)

    fun <T> setResult(key: String, data: T) {
        resultChannel.setResult(key, data)
    }

    protected fun executeCommands(vararg commands: Command) {
        commandBuffer.executeCommands(commands)
    }
}