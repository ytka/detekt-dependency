package io.ytka.detekt.dependency.model

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher

typealias PackageName = String

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


class DependencyStore(private val pathFilters : PathFilters) {
    private val dependencies = mutableMapOf<PackageName,MutableSet<PackageName>>()

    companion object {
        fun of(includes: List<String>, excludes: List<String>): DependencyStore {
            return DependencyStore(PathFilters.of(includes, excludes))
        }
    }

    fun addDependency(source: PackageName, target: PackageName?) {
        if (target == null) {
            return
        }

        if (pathFilters.isIgnored(Path.of(source)) || pathFilters.isIgnored(Path.of(target))) {
            return
        }

        dependencies.computeIfAbsent(source) { mutableSetOf() }.add(target)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        dependencies.forEach { (pkg, deps) ->
            sb.append("$pkg: ${deps.joinToString(", ")}\n")
        }
        return sb.toString()
    }
}