package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.Command
import ch.deletescape.krempl.command.argument
import ch.deletescape.krempl.command.provideDelegate
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.int

fun Builtins.Companion.exit() = object : Command("exit") {
    val code by argument("code").int().default(0)

    override fun execute() {
        exit(code)
    }
}