package io.ytka.detekt.dependency.model

class DependencyDiagram(private val dependencies: List<Dependency>) {
    fun toMermaid(): String {
        val sb = StringBuilder()
        sb.append("graph LR\n")
        dependencies.forEach { (src, deps) ->
            sb.append("    $src --> $deps\n")
        }
        return sb.toString()
    }
}