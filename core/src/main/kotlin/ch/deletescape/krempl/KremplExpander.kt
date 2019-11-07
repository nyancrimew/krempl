package ch.deletescape.krempl

import org.jline.reader.History
import org.jline.reader.impl.DefaultExpander

class KremplExpander(private var env: KremplEnvironment): DefaultExpander() {
    override fun expandVar(word: String): String {
        return env[word] ?: System.getenv(word) ?: word
    }
}