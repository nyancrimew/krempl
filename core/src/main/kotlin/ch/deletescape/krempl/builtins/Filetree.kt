package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.Command
import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.parameters.arguments.optional
import org.jline.utils.Log
import java.io.File

fun Builtins.Companion.cd() = object : Command("cd") {
    val path by argument("Path")

    override fun execute() {
        val resolved = env.resolvePath(path)
        val file = File(resolved)
        try {
            when {
                !file.exists() -> error("$path: No such file or directory")
                !file.isDirectory -> error("$path: No such file or directory")
                !file.canRead() -> error("$path: Permission denied")
                else -> env.pwd = resolved
            }
        } catch (e: SecurityException) {
            Log.debug(e)
            error("$path: Permission denied")
        }
    }
}

fun Builtins.Companion.ls() = object : Command("ls") {
    val path by argument("Path").optional()

    override fun execute() {
        val resolved = env.resolvePath(path ?: env.pwd)
        val file = File(resolved)
        try {
            when {
                !file.exists() -> error("$path: No such file or directory")
                !file.isDirectory -> error("$path: No such file or directory")
                !file.canRead() -> error("$path: Permission denied")
                else -> {
                    echo(file.list()?.sorted()?.joinToString(" "))
                }
            }
        } catch (e: SecurityException) {
            Log.debug(e)
            error("$path: Permission denied")
        }
    }
}