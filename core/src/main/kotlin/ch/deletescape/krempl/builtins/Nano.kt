package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.Command
import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.parameters.arguments.multiple
import org.jline.builtins.Commands
import org.jline.builtins.Nano
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths

fun Builtins.Companion.nano() = object : Command("nano") {
    val argv by argument().multiple()

    override fun execute() {
        Commands.nano(
            term,
            PrintStream(term.output()),
            // TODO: proper term stderr
            PrintStream(term.output()),
            Paths.get(env.pwd),
            argv.toTypedArray()
        )
    }
}