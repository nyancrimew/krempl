package ch.deletescape.krempl.utils

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.jline.utils.AttributedStyle.*

typealias StyleTransformer = AttributedStyle.() -> AttributedStyle

// Styles
@PublishedApi
internal inline val default: AttributedStyle
    get() = DEFAULT

val bold: StyleTransformer = { bold() }
val boldOff: StyleTransformer = { boldOff() }
val boldDefault: StyleTransformer = { boldDefault() }

val faint: StyleTransformer = { faint() }
val faintOff: StyleTransformer = { faintOff() }
val faintDefault: StyleTransformer = { faintDefault() }

val italic: StyleTransformer = { italic() }
val italicOff: StyleTransformer = { italicOff() }
val italicDefault: StyleTransformer = { italicDefault() }

val underline: StyleTransformer = { underline() }
val underlineOff: StyleTransformer = { underlineOff() }
val underlineDefault: StyleTransformer = { underlineDefault() }

val blink: StyleTransformer = { blink() }
val blinkOff: StyleTransformer = { blinkOff() }
val blinkDefault: StyleTransformer = { blinkDefault() }

val inverse: StyleTransformer = { inverse() }
val inverseNeg: StyleTransformer = { inverseNeg() }
val inverseOff: StyleTransformer = { inverseOff() }
val inverseDefault: StyleTransformer = { inverseDefault() }

val conceal: StyleTransformer = { conceal() }
val concealOff: StyleTransformer = { concealOff() }
val concealDefault: StyleTransformer = { concealDefault() }

val crossedOut: StyleTransformer = { crossedOut() }
val crossedOutOff: StyleTransformer = { crossedOutOff() }
val crossedOutDefault: StyleTransformer = { crossedOutDefault() }

val hidden: StyleTransformer = { hidden() }
val hiddenOff: StyleTransformer = { hiddenOff() }
val hiddenDefault: StyleTransformer = { hiddenDefault() }


// Colors
val blackFg: StyleTransformer = { foreground(BLACK) }
val blackBg: StyleTransformer = { background(BLACK) }

val grayFg: StyleTransformer = { foreground(BLACK + BRIGHT) }
val grayBg: StyleTransformer = { background(BLACK + BRIGHT) }

val redFg: StyleTransformer = { foreground(RED) }
val redBg: StyleTransformer = { background(RED) }
val brightRedFg: StyleTransformer = { foreground(RED + BRIGHT) }
val brightRedBg: StyleTransformer = { background(RED + BRIGHT) }

val greenFg: StyleTransformer = { foreground(GREEN) }
val greenBg: StyleTransformer = { background(GREEN) }
val brightGreenFg: StyleTransformer = { foreground(GREEN + BRIGHT) }
val brightGreenBg: StyleTransformer = { background(GREEN + BRIGHT) }

val yellowFg: StyleTransformer = { foreground(YELLOW) }
val yellowBg: StyleTransformer = { background(YELLOW) }
val brightYellowFg: StyleTransformer = { foreground(YELLOW + BRIGHT) }
val brightYellowBg: StyleTransformer = { background(YELLOW + BRIGHT) }

val blueFg: StyleTransformer = { foreground(BLUE) }
val blueBg: StyleTransformer = { background(BLUE) }
val brightBlueFg: StyleTransformer = { foreground(BLUE + BRIGHT) }
val brightBlueBg: StyleTransformer = { background(BLUE + BRIGHT) }

val magentaFg: StyleTransformer = { foreground(MAGENTA) }
val magentaBg: StyleTransformer = { background(MAGENTA) }
val brightMagentaFg: StyleTransformer = { foreground(MAGENTA + BRIGHT) }
val brightMagentaBg: StyleTransformer = { background(MAGENTA + BRIGHT) }

val cyanFg: StyleTransformer = { foreground(CYAN) }
val cyanBg: StyleTransformer = { background(CYAN) }
val brightCyanFg: StyleTransformer = { foreground(CYAN + BRIGHT) }
val brightCyanBg: StyleTransformer = { background(CYAN + BRIGHT) }

val whiteFg: StyleTransformer = { foreground(WHITE) }
val whiteBg: StyleTransformer = { background(WHITE) }

val defaultFg: StyleTransformer = { foregroundDefault() }
val defaultBg: StyleTransformer = { backgroundDefault() }

val foregroundOff: StyleTransformer = { foregroundOff() }
val backgroundOff: StyleTransformer = { backgroundOff() }

fun foreground(color: Int): StyleTransformer = { foreground(color) }
fun background(color: Int): StyleTransformer = { background(color) }

// Predefined styles
inline val error: StyleTransformer get() = redFg

inline operator fun StyleTransformer.plus(crossinline other: StyleTransformer): StyleTransformer =
    { this.this@plus().other() }

inline operator fun AttributedStyle.plus(transform: StyleTransformer): AttributedStyle = transform(this)

inline infix fun CharSequence.style(style: AttributedStyle) = AttributedString(this, style)
inline infix fun CharSequence.style(crossinline transform: StyleTransformer) = style(default + transform)

inline fun CharSequence.error() = style(error)
