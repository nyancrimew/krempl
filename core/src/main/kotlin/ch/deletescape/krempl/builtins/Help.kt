package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.Command
import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.parameters.arguments.optional

fun Builtins.Companion.help() = object: Command(
    "help"
) {
    private val pattern by argument("PATTERN").optional()
    private val registry by lazy { env.registry.helpRegistry }

    override fun execute() {
        if (pattern != null) {
            registry[pattern!!].forEach {
                echo(it.help)
            }
        } else {
            echo(cliktCommand.getFormattedHelp())
            echo()
            registry.listCommands().forEach {
                echo(it)
            }
        }
    }
}