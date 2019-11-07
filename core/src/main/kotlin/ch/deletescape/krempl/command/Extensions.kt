package ch.deletescape.krempl.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.output.TermUi

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

    fun build() = object : Command(
        name,
        help,
        epilog,
        invokeWithoutSubcommand,
        printHelpOnEmptyArgs,
        helpTags,
        autoCompleteEnvvar,
        aliases
    ) {
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

/**
 * Print the [message] to the screen.
 *
 * This is similar to [print] or [println], but converts newlines to the system line separator.
 *
 * @param message The message to print.
 * @param trailingNewline If true, behave like [println], otherwise behave like [print]
 * @param err If true, print to stderr instead of stdout
 */
inline fun Command.echo(
    message: Any?,
    trailingNewline: Boolean = true,
    err: Boolean = false
) = TermUi.echo(
    message,
    trailingNewline,
    err,
    cliktCommand.context.console
)

inline fun Command.error(message: Any?) = echo(message, err = true)

/**
 * Prompt a user for text input.
 *
 * If the user send a terminate signal (e,g, ctrl-c) while the prompt is active, null will be returned.
 *
 * @param text The text to display for the prompt.
 * @param default The default value to use for the input. If the user enters a newline without any other
 *   value, [default] will be returned. This parameter is a String instead of [T], since it will be
 *   displayed to the user.
 * @param hideInput If true, the user's input will not be echoed back to the screen. This is commonly used
 *   for password inputs.
 * @param requireConfirmation If true, the user will be required to enter the same value twice before it
 *   is accepted.
 * @param confirmationPrompt The text to show the user when [requireConfirmation] is true.
 * @param promptSuffix A delimiter printed between the [text] and the user's input.
 * @param showDefault If true, the [default] value will be shown as part of the prompt.
 * @param convert A callback that will convert the text that the user enters to the return value of the
 *   function. If the callback raises a [UsageError], its message will be printed and the user will be
 *   asked to enter a new value. If [default] is not null and the user does not input a value, the value
 *   of [default] will be passed to this callback.
 * @return the user's input, or null if the stdin is not interactive and EOF was encountered.
 */
fun <T> Command.prompt(
    text: String,
    default: String? = null,
    hideInput: Boolean = false,
    requireConfirmation: Boolean = false,
    confirmationPrompt: String = "Repeat for confirmation: ",
    promptSuffix: String = ": ",
    showDefault: Boolean = true,
    convert: ((String) -> T)
) = TermUi.prompt(
    text,
    default,
    hideInput,
    requireConfirmation,
    confirmationPrompt,
    promptSuffix,
    showDefault,
    cliktCommand.context.console,
    convert
)

/**
 * Prompt for user confirmation.
 *
 * Responses will be read from stdin, even if it's redirected to a file.
 *
 * @param text the question to ask
 * @param default the default, used if stdin is empty
 * @param abort if `true`, a negative answer aborts the program by raising [Abort]
 * @param promptSuffix a string added after the question and choices
 * @param showDefault if false, the choices will not be shown in the prompt.
 * @return the user's response, or null if stdin is not interactive and EOF was encountered.
 */
inline fun Command.confirm(
    text: String,
    default: Boolean = false,
    abort: Boolean = false,
    promptSuffix: String = ": ",
    showDefault: Boolean = true
) = TermUi.confirm(
    text,
    default,
    abort,
    promptSuffix,
    showDefault,
    cliktCommand.context.console
)