package io.ytka.package_characteristic.packagedeps

import java.nio.file.Path

class PackageDependencyStore(private val pathFilters : PathFilters) {
    private val dependencies = mutableMapOf<PackageName,MutableSet<PackageName>>()

    companion object {
        fun of(includes: List<String>, excludes: List<String>): PackageDependencyStore {
            return PackageDependencyStore(PathFilters.of(includes, excludes))
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

    private fun toList(): List<PackageDependency> {
        val result = mutableListOf<PackageDependency>()
        dependencies.forEach { (src, deps) ->
            deps.forEach { dep ->
                result.add(PackageDependency(src, dep))
            }
        }
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder()
        dependencies.forEach { (src, deps) ->
            sb.append("$src: ${deps.joinToString(", ")}\n")
        }
        return sb.toString()
    }

    fun toDiagram() = PackageDependencyDiagram(toList()).toMermaid()
}