package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt

class Echo : Command("echo") {
    val input by argument()

    override fun execute() {
        TermUi.echo(input)
    }
}