package ch.deletescape.krempl.keymaps

import ch.deletescape.krempl.utils.*
import org.jline.keymap.KeyMap
import org.jline.reader.Binding
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import org.jline.utils.InfoCmp

class Keymaps(private val term: Terminal) {

    val bash get() = BASH to bash()

    fun bash(): KeyMap<Binding> {
        val map = KeyMap<Binding>()
        map += LineReader.UP_LINE_OR_HISTORY to listOf(key(InfoCmp.Capability.key_up))

        return map
    }

    // simple inline helper
    private inline fun key(cap: InfoCmp.Capability) = term.key(cap)

    companion object {
        const val BASH = "bash"
    }
}