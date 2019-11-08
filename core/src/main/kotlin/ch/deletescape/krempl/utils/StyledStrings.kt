package ch.deletescape.krempl.utils

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle

// Colors
enum class Color(internal val color: Int) {
    BLACK(AttributedStyle.BLACK),
    GRAY(AttributedStyle.BRIGHT + AttributedStyle.BLACK),
    RED(AttributedStyle.RED),
    LIGHT_RED(AttributedStyle.BRIGHT + AttributedStyle.RED),
    GREEN(AttributedStyle.GREEN),
    LIGHT_GREEN(AttributedStyle.BRIGHT + AttributedStyle.GREEN),
    YELLOW(AttributedStyle.YELLOW),
    LIGHT_YELLOW(AttributedStyle.BRIGHT + AttributedStyle.YELLOW),
    BLUE(AttributedStyle.BLUE),
    LIGHT_BLUE(AttributedStyle.BRIGHT + AttributedStyle.BLUE),
    MAGENTA(AttributedStyle.MAGENTA),
    LIGHT_MAGENTA(AttributedStyle.BRIGHT + AttributedStyle.MAGENTA),
    CYAN(AttributedStyle.CYAN),
    LIGHT_CYAN(AttributedStyle.BRIGHT + AttributedStyle.CYAN),
    WHITE(AttributedStyle.WHITE),

    DEFAULT(-1),
    OFF(-2)
}

// Style DSL
class Styler(var style: AttributedStyle = Styles.DEFAULT) {
    inline fun bold() {
        style = style.bold()
    }

    inline fun boldOff() {
        style = style.boldOff()
    }

    inline fun boldDefault() {
        style = style.boldDefault()
    }

    inline fun faint() {
        style = style.faint()
    }

    inline fun faintOff() {
        style = style.faintOff()
    }

    inline fun faintDefault() {
        style = style.faintDefault()
    }

    inline fun italic() {
        style = style.italic()
    }

    inline fun italicOff() {
        style = style.italicOff()
    }

    inline fun italicDefault() {
        style = style.italicDefault()
    }

    inline fun underline() {
        style = style.underline()
    }

    inline fun underlineOff() {
        style = style.underlineOff()
    }

    inline fun underlineDefault() {
        style = style.underlineDefault()
    }

    inline fun blink() {
        style = style.blink()
    }

    inline fun blinkOff() {
        style = style.blinkOff()
    }

    inline fun blinkDefault() {
        style = style.blinkDefault()
    }

    inline fun inverse() {
        style = style.inverse()
    }

    inline fun inverseOff() {
        style = style.inverseOff()
    }

    inline fun inverseDefault() {
        style = style.inverseDefault()
    }

    inline fun inverseNeg() {
        style = style.inverseNeg()
    }

    inline fun conceal() {
        style = style.conceal()
    }

    inline fun concealOff() {
        style = style.concealOff()
    }

    inline fun concealDefault() {
        style = style.concealDefault()
    }

    inline fun crossedOut() {
        style = style.crossedOut()
    }

    inline fun crossedOutOff() {
        style = style.crossedOutOff()
    }

    inline fun crossedOutDefault() {
        style = style.crossedOutDefault()
    }

    inline fun hidden() {
        style = style.hidden()
    }

    inline fun hiddenOff() {
        style = style.hiddenOff()
    }

    inline fun hiddenDefault() {
        style = style.hiddenDefault()
    }

    // Foreground / background colors

    var foreground: Color = Color.DEFAULT
        set(color) {
            style = when (color) {
                Color.OFF -> style.foregroundOff()
                Color.DEFAULT -> style.foregroundDefault()
                else -> style.foreground(color.color)
            }
            field = color
        }

    inline var fg: Color
        get() = foreground
        set(value) {
            foreground = value
        }

    inline fun foregroundDefault() {
        style = style.foregroundDefault()
    }

    inline fun foregroundOff() {
        style = style.foregroundOff()
    }

    var background: Color = Color.DEFAULT
        set(color) {
            style = when (color) {
                Color.OFF -> style.backgroundOff()
                Color.DEFAULT -> style.backgroundDefault()
                else -> style.background(color.color)
            }
            field = color
        }

    inline var bg: Color
        get() = background
        set(value) {
            background = value
        }

    inline fun backgroundDefault() {
        style = style.backgroundDefault()
    }

    inline fun backgroundOff() {
        style = style.backgroundOff()
    }

    inline fun colorsDefault() {
        backgroundDefault()
        faintDefault()
    }
}

// Predefined styles
object Styles {
    @JvmField
    val DEFAULT = AttributedStyle.DEFAULT
    @JvmField
    val BOLD = AttributedStyle.BOLD
    @JvmField
    val ERROR = styler {
        fg = Color.RED
    }
}

inline fun CharSequence.error() = style(Styles.ERROR)

inline fun styler(parent: AttributedStyle = Styles.DEFAULT, crossinline block: Styler.() -> Unit): AttributedStyle {
    val styler = Styler(parent)
    block(styler)
    return styler.style
}

inline infix fun CharSequence.style(crossinline block: Styler.() -> Unit) = AttributedString(this, styler(block = block))
inline infix fun CharSequence.style(style: AttributedStyle) = AttributedString(this, style)

inline operator fun CharSequence.invoke(crossinline block: Styler.() -> Unit) = style(block)

inline fun CharSequence.toAttributedString() = AttributedString(this, Styles.DEFAULT)

inline operator fun AttributedString.plus(str: CharSequence) = this + str.toAttributedString()
inline operator fun CharSequence.plus(attributedString: AttributedString) = toAttributedString() + attributedString
operator fun AttributedString.plus(other: AttributedString): AttributedString =
    AttributedString.join("".toAttributedString(), this, other)

