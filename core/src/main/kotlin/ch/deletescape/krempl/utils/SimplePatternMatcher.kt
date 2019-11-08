package ch.deletescape.krempl.utils

/**
 * A matcher for simple patterns
 */
// TODO: Implement inline * wildcard, matching everything until the next section starts matching
class SimplePatternMatcher(pattern: CharSequence) {
    private val impl = compile(pattern)

    fun matches(str: CharSequence) = impl.matches(str)

    private interface MatcherImpl {
        fun matches(str: CharSequence): Boolean
    }

    private class MatchAlwaysImpl(
        private val match: Boolean
    ) : MatcherImpl {
        override fun matches(str: CharSequence) = match
    }

    private class SimpleMatcherImpl(
        private val pattern: CharSequence,
        private val anchorStart: Boolean,
        private val anchorEnd: Boolean
    ) : MatcherImpl {
        override fun matches(str: CharSequence) = when {
            anchorStart -> str.startsWith(pattern)
            anchorEnd -> str.endsWith(pattern)
            else -> str.contains(pattern)
        }
    }

    private class SectionMatcherImpl(
        private val sections: List<Section>,
        private val totalLength: Int,
        private val anchorStart: Boolean,
        private val anchorEnd: Boolean
    ) : MatcherImpl {
        override fun matches(str: CharSequence): Boolean {
            if (str.length < totalLength) return false
            val stack = sections.toMutableList()
            val iter = str.iterator()
            var sec = stack.removeAt(0)
            var idx = 0
            if (!anchorStart) {
                // Seek to the first char that matches with the first sector
                if (!sec.wildcard) {
                    for (c in iter) {
                        if (sec.matches(c, idx)) break
                    }
                }
            }
            do {
                if (stack.size == 0 && sec.wildcard) {
                    return true
                }
                for (i in idx..sec.length) {
                    if (!iter.hasNext()) return false
                    // Fast path
                    if (sec.wildcard) {
                        iter.nextChar()
                        continue
                    }
                    if (!sec.matches(iter.nextChar(), i)) return false
                }
                idx = 0
                sec = stack.removeAt(0)
            } while (stack.size >= 0)
            // If we used up the stack but aren't at the end yet we count this as a match if we
            return stack.size == 0 && (!iter.hasNext() || !anchorEnd)
        }
    }

    private class Section(
        internal val length: Int,
        private val content: CharArray? = null,
        internal val wildcard: Boolean = false
    ) {
        fun matches(c: Char, i: Int) = wildcard || content?.getOrNull(i) == c
    }

    companion object {
        private const val WILDCARD_CONT = '*'
        private const val WILDCARD_SINGLE = '?'
        private const val ESCAPE_CHAR = '\\'

        private fun compile(pattern: CharSequence): MatcherImpl {
            val anchoredStart = !pattern.startsWith(WILDCARD_CONT)
            // If a pattern isn't anchored to the start it is automatically anchored to the end
            val anchoredEnd = !pattern.endsWith(WILDCARD_CONT) && !anchoredStart

            // Remove surrounding wildcards
            val sanitizedPattern = pattern.removePrefix("*").removeSuffix("*")

            return if (sanitizedPattern.contains(WILDCARD_SINGLE)) {
                var lenTotal = 0
                val sections = mutableListOf<Section>()
                var escapeFlag = false
                var wildcardFlag = false
                var sectionLen = 0
                val content = mutableListOf<Char>()
                fun newSection() {
                    if (sectionLen > 0) {
                        sections += if (wildcardFlag) {
                            Section(sectionLen, wildcard = true)
                        } else Section(sectionLen, content = content.toCharArray())
                    }
                    lenTotal += sectionLen
                    sectionLen = 0
                    content.clear()
                }
                for (c in sanitizedPattern) {
                    sectionLen++
                    when (c) {
                        ESCAPE_CHAR -> {
                            if (escapeFlag) {
                                content.add(c)
                            } else {
                                sectionLen--
                                if (wildcardFlag) {
                                    newSection()
                                    wildcardFlag = false
                                }
                            }
                            escapeFlag = !escapeFlag
                        }
                        WILDCARD_SINGLE -> {
                            if (!wildcardFlag) {
                                newSection()
                                wildcardFlag = true
                            } else if (escapeFlag) {
                                content.add(c)
                            }
                        }
                        else -> {
                            if (wildcardFlag) {
                                newSection()
                                wildcardFlag = false
                            }
                            content.add(c)
                        }
                    }
                }
                newSection()
                if (sections.size == 1) {
                    if (sections.first().wildcard)
                        MatchAlwaysImpl(true)
                    else SimpleMatcherImpl(sanitizedPattern, anchoredStart, anchoredEnd)
                } else SectionMatcherImpl(sections, lenTotal, anchoredStart, anchoredEnd)
            } else {
                SimpleMatcherImpl(sanitizedPattern, anchoredStart, anchoredEnd)
            }
        }
    }
}