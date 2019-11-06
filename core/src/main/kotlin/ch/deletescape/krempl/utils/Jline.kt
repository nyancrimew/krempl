package ch.deletescape.krempl.utils

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

inline fun terminal(parent: TerminalBuilder? = null, crossinline block: TerminalBuilder.() -> Unit): TerminalBuilder {
    val builder = parent ?: TerminalBuilder.builder()
    block(builder)
    return builder
}

fun Terminal.println(message: String) = writer().println(message)
fun Terminal.print(message: String) = writer().print(message)
fun Terminal.errorln(message: String) = writer().errorln(message)
fun Terminal.error(message: String) = writer().error(message)
fun Terminal.echoln(message: String, err: Boolean = false) = if (err) errorln(message) else println(message)
fun Terminal.echo(message: String, err: Boolean = false) = if (err) error(message) else print(message)

private var reader: LineReader? = null
private fun Terminal.lineReader(): LineReader {
    if (reader == null || reader?.terminal != this) {
        reader = LineReaderBuilder.builder()
            .terminal(this)
            .build()
    }
    return reader!!
}

fun Terminal.readLine(prompt: String = "", hidden: Boolean = false): String {
    echo(prompt)
    return lineReader().readLine(if (hidden) '*' else null)
}