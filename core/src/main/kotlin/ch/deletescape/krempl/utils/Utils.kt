package ch.deletescape.krempl.utils

import org.jline.utils.AttributedStyle
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1

inline fun <T> T.chainIf(condition: Boolean, crossinline block: T.() -> T): T = if (condition) {
    run(block)
} else this

inline fun <T, P> T.chainIfNotNull(target: P?, crossinline block: T.(P) -> T): T = chainIf(target != null) {
    block(target!!)
}

inline fun <T, reified O> T.convert(crossinline block: T.() -> O): O = if (this !is O) {
    block()
} else this
