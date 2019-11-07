package ch.deletescape.krempl.prompt

import ch.deletescape.krempl.KremplEnvironment
import ch.deletescape.krempl.utils.brightBlueFg
import ch.deletescape.krempl.utils.convert
import ch.deletescape.krempl.utils.style
import org.jline.terminal.Terminal
import org.jline.utils.AttributedCharSequence
import org.jline.utils.AttributedString

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
            "$user@$hostname $name ${collapseHome(pwd)} > " style brightBlueFg
        }
    }
}
