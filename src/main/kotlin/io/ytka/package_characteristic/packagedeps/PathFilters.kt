package io.ytka.package_characteristic.packagedeps

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher

class PathFilters(
    private val includes: Set<PathMatcher>?,
    private val excludes: Set<PathMatcher>?
) {

    fun isIgnored(path: Path): Boolean {
        fun isIncluded() = includes?.any { it.matches(path) } ?: true
        fun isExcluded() = excludes?.any { it.matches(path) } ?: false

        return !(isIncluded() && !isExcluded())
    }

    companion object {
        fun of(includes: List<String>, excludes: List<String>): PathFilters {
            return PathFilters(parse(includes), parse(excludes))
        }

        private fun parse(value: List<String>): Set<PathMatcher>? =
            if (value.isEmpty()) {
                null
            } else {
                value
                    .map { pathMatcher(it) }
                    .toSet()
            }
    }
}

private fun pathMatcher(pattern: String): PathMatcher {
    val result = when (pattern.substringBefore(":")) {
        "glob" -> pattern
        "regex" -> throw IllegalArgumentException(USE_GLOB_MSG)
        else -> "glob:$pattern"
    }

    return FileSystems.getDefault().getPathMatcher(result)
}

private const val USE_GLOB_MSG =
    "Only globbing patterns are supported as they are treated os-independently by the PathMatcher api."
