package main

import ch.deletescape.krempl.Krempl
import ch.deletescape.krempl.builtins.Builtins
import ch.deletescape.krempl.builtins.cd
import ch.deletescape.krempl.builtins.echo
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

fun main() {
    val code = Krempl {
        commands(
            Builtins.pwd(),
            Builtins.whoami(),
            Builtins.cd(),
            Builtins.echo()
        )
    }.start()
    if (code != 0) {
        exitProcess(code)
    }

}