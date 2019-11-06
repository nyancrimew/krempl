package ch.deletescape.krempl

import ch.deletescape.krempl.prompt.Prompt
import ch.deletescape.krempl.utils.terminal
import org.jline.terminal.TerminalBuilder

data class KremplConfig(
    var term: TerminalBuilder,
    var prompt: Prompt
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

    var name: String
        get() = term.build().name
        set(value) {
            term.name(value)
        }

    companion object {
        val DEFAULT = KremplConfig(
            term = terminal {
                name("Krempl")
                system(true)
                jna(true)
            },
            prompt = Prompt.Default
        )
    }
}

inline fun config(parent: KremplConfig? = null, crossinline block: KremplConfig.() -> Unit): KremplConfig {
    val config = parent ?: KremplConfig.DEFAULT
    block(config)
    return config
}