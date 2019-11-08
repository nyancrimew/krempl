package main

import ch.deletescape.krempl.Krempl
import ch.deletescape.krempl.builtins.*
import kotlin.system.exitProcess

fun main() {
    val code = Krempl {
        commands(Builtins.all)
    }.start()
    if (code != 0) {
        exitProcess(code)
    }

}