package ch.deletescape.krempl.command

import ch.deletescape.krempl.KremplEnvironment
import ch.deletescape.krempl.utils.echo
import ch.deletescape.krempl.utils.readLine
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.CliktConsole
import org.jline.terminal.Terminal

class CommandRegistry(term: Terminal, private val env: KremplEnvironment) {
    private val baseCommand = BaseCommand(term, env)
    private val aliases = mutableSetOf<Pair<String, String>>()

    fun registerCommand(command: Command) {
        baseCommand.subcommands(command.cliktCommand)
        command.onRegister(env)
    }

    fun registerCommands(vararg commands: Command) {
        for (command in commands) {
            registerCommand(command)
        }
    }

    fun setAlias(name: String, command: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) throw IllegalArgumentException("Alias name cannot be empty")
        aliases += trimmed to command
    }

    fun removeAliases(name: String) {
        aliases.removeIf {
            it.first == name
        }
    }

    fun parseAndDispatch(args: List<String>) {
        baseCommand.parse(args)
    }

    private inner class BaseCommand(term: Terminal, env: KremplEnvironment) : CliktCommand(name = "") {
        init {
            context {
                console = object : CliktConsole {
                    override fun promptForLine(prompt: String, hideInput: Boolean): String? {
                        return try {
                            term.readLine(prompt, hideInput)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    override fun print(text: String, error: Boolean) = term.echo(text, error)
                    override val lineSeparator = env.lineSeperator!!
                }
            }
        }

        override fun getFormattedHelp(): String {
            return """
            TODO
        """.trimIndent()
        }

        override fun getFormattedUsage(): String {
            return """
            TODO
        """.trimIndent()
        }

        override fun run() {
            // do nothing
        }

        override fun aliases(): Map<String, List<String>> {
            return aliases.groupBy({ it.first }) { it.second }
        }
    }
}
