package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

fun Builtins.Companion.echo() = object : Command("echo") {
    val noNewline by option("-n").flag()
    val args by argument().multiple()

    override fun execute() {
        echo(args.joinToString(" "), trailingNewline = !noNewline)
    }
}