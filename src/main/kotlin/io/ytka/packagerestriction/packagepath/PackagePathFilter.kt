package io.ytka.packagerestriction.packagepath

import java.nio.file.FileSystems
import java.nio.file.PathMatcher

typealias PackagePathPattern = String

private fun complementFilterPattern(pattern: String): String {
    //if (pattern.endsWith("*")) {
    //    return pattern
    //}
    // Add wildcard to match subpackages
    //return "$pattern{,.*}"
    return pattern
}

class PackagePathFilter(pathPattern: PackagePathPattern) {
    private val globPathPattern = "glob:${complementFilterPattern(pathPattern)}"
    private val pathMatcher: PathMatcher = FileSystems.getDefault().
        getPathMatcher(globPathPattern)

    fun matches(packagePath: String): Boolean {
        return pathMatcher.matches(FileSystems.getDefault().getPath(packagePath))
    }

    override fun toString(): String {
        return "PackagePathFilter($globPathPattern)"
    }
}