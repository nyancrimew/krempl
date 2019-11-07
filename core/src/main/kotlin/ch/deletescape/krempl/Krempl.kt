package ch.deletescape.krempl

import ch.deletescape.krempl.builtins.Builtins
import ch.deletescape.krempl.builtins.cd
import ch.deletescape.krempl.builtins.echo
import ch.deletescape.krempl.builtins.exit
import ch.deletescape.krempl.command.*
import ch.deletescape.krempl.keymaps.Keymaps
import ch.deletescape.krempl.utils.bind
import ch.deletescape.krempl.utils.rawLine
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi.echo
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jline.builtins.Widgets
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.reader.impl.LineReaderImpl
import org.jline.utils.Log
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class Krempl constructor(private val config: KremplConfig = KremplConfig()) {
    constructor(block: KremplConfig.() -> Unit) : this(config(block = block))

    val term = config.term.build()
    val env = KremplEnvironment(config)
    val prompt = config.prompt
    // Setup reader with our defaults
    val reader = LineReaderBuilder.builder()
        .expander(KremplExpander(env))
        .option(LineReader.Option.AUTO_FRESH_LINE, true)
        .variable(LineReader.SECONDARY_PROMPT_PATTERN, ">")
        .variable(LineReader.HISTORY_FILE, env.kremplFile("history"))
        .terminal(term)
        .build()
    val registry = CommandRegistry(term, env)
    val keymaps = Keymaps(term)

    fun start(): Int = runBlocking {
        registry.registerCommands(config.commands)
        (reader.parser as DefaultParser).apply {
            isEofOnUnclosedQuote = true
            isEofOnEscapedNewLine = true
        }
        reader.autosuggestion = LineReader.SuggestionType.HISTORY
        while (true) {
            try {
                val line = reader.readLine(prompt.create(env, term))
                val pl = reader.parser.parse(line, 0)
                registry.parseAndDispatch(pl.words())
            } catch (e: PrintHelpMessage) {
                echo(e.command.getFormattedHelp())
            } catch (e: PrintMessage) {
                echo(e.message)
            } catch (e: UsageError) {
                echo(e.helpMessage(), err = true)
            } catch (e: CliktError) {
                echo(e.message, err = true)
            } catch (e: Abort) {
                echo("Aborted!", err = true)
            } catch (e: UserInterruptException) {
                // ignore
            } catch (e: EndOfFileException) {
                break
            } catch (e: Exception) {
                echo(e.message, err = true)
            }
        }
        reader.history.save()
        return@runBlocking env.exitCode
    }
}