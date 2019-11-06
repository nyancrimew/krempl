package ch.deletescape.krempl

import ch.deletescape.krempl.builtins.Builtins
import ch.deletescape.krempl.builtins.Echo
import ch.deletescape.krempl.command.*
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import kotlinx.coroutines.runBlocking
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import kotlin.system.exitProcess

class Krempl constructor(private val config: KremplConfig = KremplConfig.DEFAULT) {
    constructor(block: KremplConfig.() -> Unit) : this(config(block = block))

    val term = config.term.build()
    val env = KremplEnvironment(config)
    val prompt = config.prompt
    val reader = LineReaderBuilder.builder()
        .terminal(term)
        .build()
    val registry = CommandRegistry(term, env)

    fun start() = runBlocking {
        registry.registerCommands(
            Echo()
        )
        while (true) {
            try {
                val line = reader.readLine(prompt.create(env))
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
                return@runBlocking
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}