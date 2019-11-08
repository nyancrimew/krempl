package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.command
import ch.deletescape.krempl.command.echo

class Builtins {
    companion object {
        @JvmField
        val base = setOf(
            exit(),
            help()
        )

        @JvmField
        val all = base + setOf(
            echo(),
            pwd(),
            whoami(),
            cd()
        )


        fun pwd() = command("pwd") {
            action {
                echo(env.pwd)
            }
        }

        fun whoami() = command("whoami") {
            action {
                echo(env.user)
            }
        }
    }
}