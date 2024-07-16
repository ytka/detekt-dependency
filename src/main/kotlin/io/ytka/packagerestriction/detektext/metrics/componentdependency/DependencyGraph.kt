package io.ytka.packagerestriction.detektext.metrics.componentdependency
import java.util.*


typealias ComponentID = String

class ComponentDependencyGraph {
    private val vertexes = mutableSetOf<ComponentID>()
    private val edges = mutableSetOf<Pair<ComponentID, ComponentID>>()

    fun components() : Set<ComponentID> = vertexes

    fun addDependency(from: ComponentID, to: ComponentID) {
        vertexes.add(from)
        vertexes.add(to)
        edges.add(from to to)
    }

    fun calculate() : CalculatedComponentDependency {
        val attributes = calculateComponentAttributes()

        val componentsCount = components().size
        val ccd = attributes.values.sumOf { it.componentDependencyValue }

        return CalculatedComponentDependency(
            componentAttributes = attributes,
            ccd = ccd,
            acd = ccd.toDouble() / componentsCount,
            pc = ccd.toDouble() / componentsCount / componentsCount
        )
    }

    private fun calculateComponentAttributes() : Map<ComponentID, CalculatedComponentAttributes> {
        val attributes = mutableMapOf<ComponentID, CalculatedComponentAttributes>()

        for (vertex in vertexes) {
            val reachableVertices = getReachableVertices(vertex)
            attributes[vertex] = CalculatedComponentAttributes(componentDependencyValue = reachableVertices.size)
        }
        return attributes
    }

    private fun getReachableVertices(startVertex: ComponentID): Set<String> {
        val visited: MutableSet<ComponentID> = HashSet()
        val queue: Queue<ComponentID> = LinkedList()
        queue.add(startVertex)

        while (!queue.isEmpty()) {
            val vertex = queue.poll()
            if (!visited.contains(vertex)) {
                visited.add(vertex)
                val edges = edges.filter { it.first == vertex }
                for (edge in edges) {
                    val targetVertex = edge.second
                    if (!visited.contains(targetVertex)) {
                        queue.add(targetVertex)
                    }
                }
            }
        }

        return visited
    }

    override fun toString(): String {
        val b = StringBuilder()
        b.append("Vertices:${vertexes.joinToString { it }} \n")
        b.append("Edges:\n")
        for (edge in edges) {
            val source = edge.first
            val target = edge.second
            b.append("$source -> $target\n")
        }
        return b.toString()
    }
}

data class CalculatedComponentAttributes(
    val componentDependencyValue: Int,
) {
    override fun toString(): String {
        return "componentDependencyValue=$componentDependencyValue"
    }

}

data class CalculatedComponentDependency(
    val componentAttributes: Map<ComponentID,CalculatedComponentAttributes>,
    val componentCount: Int = componentAttributes.size,
    val ccd: Int, // Cumulative Component Dependency：CCD
    val acd: Double, // Average Component Dependency：ACD
    val pc: Double, // Propagation Cost：PC
) {
    override fun toString(): String {
        val b = StringBuilder()
        b.append("ComponentDependency: componentCount=$componentCount, CCD=$ccd, ACD=$acd, PC=$pc\n")
        for ((component, attributes) in componentAttributes) {
            b.append("    $component: ${attributes.componentDependencyValue}\n")
        }
        return b.toString()
    }
}