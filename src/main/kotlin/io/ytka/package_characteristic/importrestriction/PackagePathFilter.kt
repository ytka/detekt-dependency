package io.ytka.package_characteristic.importrestriction

import java.nio.file.FileSystems
import java.nio.file.PathMatcher

typealias PackagePathPattern = String

class PackagePathFilter(pathPattern: PackagePathPattern) {
    private val pathMatcher: PathMatcher = FileSystems.getDefault().
        getPathMatcher("glob:${complementFilterPattern(pathPattern)}")

    fun matches(packagePath: String): Boolean {
        return pathMatcher.matches(FileSystems.getDefault().getPath(packagePath))
    }
}

private fun complementFilterPattern(pattern: String): String {
    if (pattern.endsWith("*")) {
        return pattern
    }
    // Add wildcard to match subpackages
    return "$pattern{,.*}"
}