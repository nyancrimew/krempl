package main

import ch.deletescape.krempl.Krempl
import ch.deletescape.krempl.builtins.*
import kotlin.system.exitProcess

fun main() {
    val code = Krempl {
        commands(
            Builtins.echo(),
            Builtins.pwd(),
            Builtins.nano()
        )
    }.start()
    if (code != 0) {
        exitProcess(code)
    }

}