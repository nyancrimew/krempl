package ch.deletescape.krempl.command

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.options.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublishedApi
internal fun <T : Any> defaultArgAllProcessor(): ArgCallsTransformer<T, T> = { it.single() }

@PublishedApi
internal fun <T> defaultArgValidator(): ArgValidator<T> = {}

@PublishedApi
internal fun <T : Any> defaultOptionEachProcessor(): ArgsTransformer<T, T> = { it.single() }

@PublishedApi
internal fun <T : Any> defaultOptionAllProcessor(): CallsTransformer<T, T?> = { it.lastOrNull() }

@PublishedApi
internal fun <T> defaultOptionValidator(): OptionValidator<T> = { }

internal typealias RawArgument = ProcessedArgument<String, String>

fun Command.argument(
    name: String = "",
    help: String = "",
    helpTags: Map<String, String> = emptyMap()
): RawArgument = ProcessedArgument(
    name = name,
    nvalues = 1,
    required = true,
    help = help,
    helpTags = helpTags,
    completionCandidates = CompletionCandidates.None,
    transformValue = { it },
    transformAll = defaultArgAllProcessor(),
    transformValidator = defaultArgValidator()
)

inline fun <T : Any> Command.mutuallyExclusiveOptions(
    option1: OptionDelegate<T?>,
    option2: OptionDelegate<T?>,
    vararg options: OptionDelegate<T?>,
    name: String? = null,
    help: String? = null
) = cliktCommand.mutuallyExclusiveOptions(
    option1 = option1,
    option2 = option2,
    options = *options,
    name = name,
    help = help
)

inline fun Command.versionOption(
    version: String,
    help: String = "Show the version and exit",
    names: Set<String> = setOf("--version"),
    crossinline message: (String) -> String = { "$name version $it" }
) = apply {
    cliktCommand.versionOption(version, help, names, message)
}

fun Command.context(block: Context.Builder.() -> Unit) = apply {
    cliktCommand.context(block)
}

fun Command.subcommands(vararg commands: Command) = apply {
    cliktCommand.subcommands(commands.map { it.cliktCommand })
}

operator fun <T> ArgumentDelegate<T>.provideDelegate(thisRef: Command, prop: KProperty<*>): ReadOnlyProperty<Command, T> {
    // ensure stock logic runs as well
    this.provideDelegate(thisRef.cliktCommand, prop)
    return ArgumentDelegateWrapper(this)
}

class ArgumentDelegateWrapper<out T>(private val wrapped: ArgumentDelegate<T>) : Argument, ReadOnlyProperty<Command, T> {
    override fun getValue(thisRef: Command, property: KProperty<*>): T {
        return wrapped.getValue(thisRef.cliktCommand, property)
    }

    override val name = wrapped.name
    override val help = wrapped.help
    override val helpTags = wrapped.helpTags
    override val nvalues = wrapped.nvalues
    override val parameterHelp = wrapped.parameterHelp
    override val required = wrapped.required

    override fun finalize(context: Context, values: List<String>) {
        wrapped.finalize(context, values)
    }

    override fun postValidate(context: Context) {
        wrapped.postValidate(context)
    }
}