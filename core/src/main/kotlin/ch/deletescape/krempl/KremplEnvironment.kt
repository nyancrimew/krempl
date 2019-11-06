package ch.deletescape.krempl

import ch.deletescape.krempl.utils.property
import com.kstruct.gethostname4j.Hostname

class KremplEnvironment(config: KremplConfig) {
    val name = config.name
    val user by property("user.name", useCache = true)
    val homeDir by property("user.home", useCache = true)
    var pwd by property("user.dir")
        internal set
    val lineSeperator by property("line.seperator", "\n", useCache = true)
    val hostname = Hostname.getHostname()
}