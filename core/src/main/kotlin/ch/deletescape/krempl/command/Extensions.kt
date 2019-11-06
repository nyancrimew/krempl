package ch.deletescape.krempl.command

class DslCommandBuilder(val name: String) {
    var help: String = ""
    var epilog: String = ""
    var invokeWithoutSubcommand: Boolean = false
    var printHelpOnEmptyArgs: Boolean = false
    var helpTags: Map<String, String> = emptyMap()
    var autoCompleteEnvvar: String? = ""
    var aliases = mutableListOf<String>()

    var action: Command.() -> Unit = {}
    fun action(block: Command.() -> Unit) {
        action = block
    }

    fun build() = object : Command(name, help, epilog, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar, aliases) {
        override fun execute() {
            action(this)
        }
    }
}

inline fun CommandRegistry.registerCommand(name: String, crossinline block: DslCommandBuilder.() -> Unit): Command {
    val command = command(name, block)
    registerCommand(command)
    return command
}

// TODO: make arguments work here lol
inline fun command(name: String, crossinline block: DslCommandBuilder.() -> Unit): Command {
    val builder = DslCommandBuilder(name)
    block(builder)
    return builder.build()
}