package ch.deletescape.krempl.prompt

import ch.deletescape.krempl.KremplEnvironment
import ch.deletescape.krempl.utils.*
import org.jline.terminal.Terminal
import org.jline.utils.AttributedCharSequence

inline class Prompt(inline val create: KremplEnvironment.() -> CharSequence) {
    constructor(prompt: CharSequence) : this({ prompt })

    fun create(env: KremplEnvironment, term: Terminal) = env.create().convert<CharSequence, String> {
        when (this) {
            is AttributedCharSequence -> toAnsi(term)
            else -> toString()
        }
    }

    companion object {
        val Default = Prompt {
            "$user@$hostname " { bold(); fg = Color.LIGHT_GREEN } +
                    name { fg = Color.BLUE; faint() } + " " +
                    collapseHome(pwd).style { fg = Color.LIGHT_YELLOW } + " > "
        }
    }
}
