package io.ytka.package_characteristic.packagedeps

class PackageDependencyDiagram(private val dependencies: List<PackageDependency>) {
    fun toMermaid(): String {
        val sb = StringBuilder()
        sb.append("graph LR\n")
        dependencies.forEach { (src, deps) ->
            sb.append("    $src --> $deps\n")
        }
        return sb.toString()
    }
}