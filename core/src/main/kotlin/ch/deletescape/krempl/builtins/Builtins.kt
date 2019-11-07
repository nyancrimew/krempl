package ch.deletescape.krempl.builtins

import ch.deletescape.krempl.command.command
import ch.deletescape.krempl.command.echo

class Builtins {
    companion object {
        fun all() = listOf(
            exit(),
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