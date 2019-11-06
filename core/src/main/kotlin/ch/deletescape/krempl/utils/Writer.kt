package ch.deletescape.krempl.utils

import java.io.PrintWriter

fun PrintWriter.errorln(message: String) = println(message.error())
fun PrintWriter.error(message: String) = print(message.error())