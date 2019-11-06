package ch.deletescape.krempl.utils

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle

val errorStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
fun String.error() = AttributedString(this, errorStyle)