package ch.deletescape.krempl.prompt

import ch.deletescape.krempl.KremplEnvironment

inline class Prompt(val create: KremplEnvironment.() -> String) {
    constructor(prompt: String) : this({ prompt })
    companion object {
        val Default = Prompt {
            "$user@$hostname $name ${collapseHome(pwd)} > "
        }
    }
}
