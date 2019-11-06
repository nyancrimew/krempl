package ch.deletescape.krempl.command

import ch.deletescape.krempl.KremplEnvironment
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.GroupableOption
import com.github.ajalt.clikt.core.ParameterHolder
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlin.reflect.KProperty


abstract class Command(
    val name: String,
    help: String = "",
    epilog: String = "",
    invokeWithoutSubcommand: Boolean = false,
    printHelpOnEmptyArgs: Boolean = false,
    helpTags: Map<String, String> = emptyMap(),
    autoCompleteEnvvar: String? = "",
    val aliases: List<String> = emptyList()
) : ParameterHolder {
    @PublishedApi
    internal val cliktCommand =
        CliktWrapper(name, help, epilog, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar)
    protected lateinit var env: KremplEnvironment
        private set

    init {
        if (name.isBlank()) throw IllegalArgumentException("Command name cannot be empty")
    }

    abstract fun execute()

    internal fun onRegister(env: KremplEnvironment) {
        this.env = env
    }

    override fun registerOption(option: GroupableOption) {
        cliktCommand.registerOption(option)
    }

    internal inner class CliktWrapper(
        name: String,
        help: String = "",
        epilog: String = "",
        invokeWithoutSubcommand: Boolean = false,
        printHelpOnEmptyArgs: Boolean = false,
        helpTags: Map<String, String> = emptyMap(),
        autoCompleteEnvvar: String? = ""
    ) : CliktCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {
        override fun run() {
            this@Command.execute()
        }

        override fun aliases(): Map<String, List<String>> {
            return mapOf(this@Command.name to this@Command.aliases)
        }
    }
}