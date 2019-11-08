package ch.deletescape.krempl

import ch.deletescape.krempl.command.CommandRegistry
import ch.deletescape.krempl.utils.expandHome
import ch.deletescape.krempl.utils.sysProp
import com.kstruct.gethostname4j.Hostname
import java.io.File
import java.nio.file.Paths

class KremplEnvironment(config: KremplConfig) {
    val name = config.name
    val user by sysProp("user.name", useCache = true)
    val homeDir by sysProp("user.home", useCache = true)
    var pwd by sysProp("user.dir")
        internal set
    val lineSeperator by sysProp("line.seperator", "\n", useCache = true)
    val hostname = Hostname.getHostname()
    var exitCode = 0
        set(value) {
            // Only allow changing the exit Code if it is still zero
            if (field == 0) {
                field = value
            }
        }
    val kremplDir = config.kremplDir

    operator fun get(key: String) = when (key.toLowerCase()) {
        "shell" -> name
        "user" -> user
        "username" -> user
        "~" -> homeDir
        "home" -> homeDir
        "homepath" -> homeDir
        "pwd" -> pwd
        "hostname" -> hostname
        else -> null
    }

    fun collapseHome(path: String) = if (path.startsWith(homeDir)) path.replaceFirst(homeDir, "~") else path
    fun resolvePath(path: String) = File(pwd).resolve(expandHome(path)).normalize().toString()
    fun kremplFile(path: String) = File(kremplDir).resolve(path)


    // Krempl internal properties
    internal lateinit var registry: CommandRegistry
}