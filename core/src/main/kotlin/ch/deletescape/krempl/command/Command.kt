package ch.deletescape.krempl.command

import ch.deletescape.krempl.KremplEnvironment
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.GroupableOption
import com.github.ajalt.clikt.core.ParameterHolder
import org.jline.reader.EndOfFileException
import org.jline.terminal.Terminal


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
    lateinit var env: KremplEnvironment
        private set
    internal lateinit var term: Terminal

    init {
        if (name.isBlank()) throw IllegalArgumentException("Command name cannot be empty")
    }

    abstract fun execute()

    /**
     * Close terminal and exit with the provided exit code
     */
    fun exit(code: Int = 0) {
        env.exitCode = code
        term.close()
        // FIXME: TERRIBLE HACK
        throw EndOfFileException()
    }

    internal fun onRegister(env: KremplEnvironment, term: Terminal) {
        this.env = env
        this.term = term
    }

    override fun registerOption(option: GroupableOption) {
        cliktCommand.registerOption(option)
    }

    override fun equals(other: Any?): Boolean {
        return other is Command && name == other.name
    }

    override fun hashCode(): Int {
        return "command-$name".hashCode()
    }

    @PublishedApi
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