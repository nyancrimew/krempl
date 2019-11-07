package ch.deletescape.krempl.utils

import java.io.File
import kotlin.reflect.KProperty

fun sysProp(key: String, default: String = "", useCache: Boolean = true) = SystemProperty(key, default, useCache)

class SystemProperty(private val key: String, private val default: String = "", private val useCache: Boolean = false) {
    private var cached = false
    private var cache: String? = null
        set(value) {
            field = value
            cached = true
        }
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
        if (useCache && cached) {
            return cache!!
        }
        return System.getProperty(key, default).also {
            if (useCache) {
                cache = it
            }
        }
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        System.setProperty(key, value)
        if (useCache) {
            cache = value
        }
    }
}

fun expandHome(path: String, home: String = System.getProperty("user.home")) = when {
    path == "~" -> home
    path.startsWith("~" + File.separator) -> home + path.substring(1)
    // Windows + developers is always a fun one
    path.startsWith("~/") -> System.getProperty("user.home") + path.substring(1)
    path.startsWith("~") -> TODO("Home dir expansion not implemented for explicit usernames")
    else -> path
}