package ch.deletescape.krempl

import ch.deletescape.krempl.builtins.Builtins
import ch.deletescape.krempl.builtins.exit
import ch.deletescape.krempl.command.Command
import ch.deletescape.krempl.prompt.Prompt
import ch.deletescape.krempl.utils.expandHome
import ch.deletescape.krempl.utils.terminal
import org.jline.builtins.Commands
import org.jline.terminal.TerminalBuilder

data class KremplConfig(
    var name: String = DEFAULT_NAME,
    var term: TerminalBuilder = DEFAULT_TERM,
    var prompt: Prompt = Prompt.Default,
    var kremplDir: String = expandHome("~/.krempl"),
    var commands: Set<Command> = Builtins.base
) {
    inline fun term(crossinline block: TerminalBuilder.() -> Unit) {
        term = terminal(term, block)
    }

    fun prompt(prompt: KremplEnvironment.() -> String) {
        this.prompt = Prompt(prompt)
    }

    fun prompt(prompt: String) {
        this.prompt = Prompt(prompt)
    }

    fun commands(vararg commands: Command) {
        this.commands += commands
    }

    fun commands(commands: Iterable<Command>) {
        this.commands += commands
    }

    companion object {
        val DEFAULT_NAME = "Krempl"
        val DEFAULT_TERM = terminal {
            name(DEFAULT_NAME)
            system(true)
            jna(true)
        }
    }
}

inline fun config(crossinline block: KremplConfig.() -> Unit): KremplConfig {
    val config = KremplConfig()
    block(config)
    return config
}