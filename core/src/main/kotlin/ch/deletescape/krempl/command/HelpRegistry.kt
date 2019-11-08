package ch.deletescape.krempl.command

import ch.deletescape.krempl.utils.*
import org.jline.utils.AttributedString

class HelpRegistry {
    private val entries = mutableMapOf<String, HelpEntry>()
    fun registerCommand(command: Command) {
        val help = command.cliktCommand.getFormattedHelp()
        val usage = command.cliktCommand.getFormattedUsage()
        val shortHelp = command.cliktCommand.shortHelp

        for (name in command.aliases + command.name) {
            entries += name to HelpEntry(help, usage, shortHelp)
        }
    }

    operator fun get(pattern: String): List<HelpEntry> {
        val matcher = SimplePatternMatcher(pattern)
        return entries.filterKeys {
            matcher.matches(it)
        }.values.toList()
    }

    fun listCommands(): List<AttributedString> = entries.map {
        it.key { bold() } + " " + it.value.shortHelp
    }
}

data class HelpEntry(val help: String, val usage: String, val shortHelp: String)