package ch.deletescape.krempl.utils

import org.jline.keymap.KeyMap
import org.jline.reader.Binding
import org.jline.reader.Reference
import org.jline.reader.Widget
import org.jline.terminal.Terminal
import org.jline.utils.InfoCmp

open class KeyBinding(val widget: Binding, val keys: Iterable<CharSequence>) {
    constructor(widget: String, keys: Iterable<CharSequence>): this(Reference(widget), keys)
}

infix fun String.to(keys: Iterable<CharSequence>) = KeyBinding(this, keys)
infix fun Widget.to(keys: Iterable<CharSequence>) = KeyBinding(this, keys)

infix fun KeyMap<Binding>.bind(binding: KeyBinding) = bind(binding.widget, binding.keys)
operator fun KeyMap<Binding>.plusAssign(binding: KeyBinding) {
    bind(binding)
}

fun Terminal.key(capability: InfoCmp.Capability) = KeyMap.key(this, capability)
