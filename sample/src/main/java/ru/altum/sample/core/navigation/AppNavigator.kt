package ru.altum.sample.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.altum.composione.command.Back
import ru.altum.composione.command.Command
import ru.altum.composione.command.Forward
import ru.altum.composione.destination.Destination
import ru.altum.composione.navigator.Navigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppNavigator @Inject constructor(startDestination: Destination) : Navigator {

    override val backStack: SnapshotStateList<Destination> = mutableStateListOf(startDestination)

    override fun applyCommands(commands: Array<out Command>) {
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun applyCommand(command: Command) {
        when (command) {
            is Back -> {
                backStack.removeAt(backStack.lastIndex)
            }

            is BackTo -> {
                val index: Int = backStack.indexOfFirst { it.key == command.key }
                if (index != -1) {
                    backStack.retainAll(backStack.subList(0, index + 1))
                }
            }

            is BackToRoot -> {
                backStack.retainAll(listOf(backStack.first()))
            }

            is Forward -> {
                backStack.add(command.destination)
            }

            is NewRoot -> {
                backStack.apply {
                    clear()
                    add(command.destination)
                }
            }

            is FromRootTo -> {
                backStack.retainAll(listOf(backStack.first(), command.destination))
            }
        }
    }

}