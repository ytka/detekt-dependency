package io.ytka.detektrules.package_characteristic.model

import java.nio.file.Path

typealias PackageName = String
typealias Dependency = Pair<PackageName, PackageName>

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

    fun toList(): List<Dependency> {
        val result = mutableListOf<Dependency>()
        dependencies.forEach { (src, deps) ->
            deps.forEach { dep ->
                result.add(Pair(src, dep))
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

    fun toDiagram() = DependencyDiagram(toList()).toMermaid()
}