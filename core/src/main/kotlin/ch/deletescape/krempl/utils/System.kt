package ch.deletescape.krempl.utils

import kotlin.reflect.KProperty

fun property(key: String, default: String? = null, useCache: Boolean = true) = SystemProperty(key, default, useCache)

class SystemProperty(private val key: String, private val default: String? = null, private val useCache: Boolean = false) {
    private var cached = false
    private var cache: String? = null
        set(value) {
            field = value
            cached = true
        }
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String? {
        if (useCache && cached) {
            return cache
        }
        return System.getProperty(key, default).also {
            if (useCache) {
                cache = it
            }
        }
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String?) {
        System.setProperty(key, value)
        if (useCache) {
            cache = value
        }
    }
}