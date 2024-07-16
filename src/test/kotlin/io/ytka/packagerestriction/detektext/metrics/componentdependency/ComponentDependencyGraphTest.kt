package io.ytka.packagerestriction.detektext.metrics.componentdependency

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComponentDependencyGraphTest {
    @Test
    fun testAddDependency() {
        val graph = ComponentDependencyGraph()
        val component1 = "1"
        val component2 = "2"
        graph.addDependency(component1, component2)
        assertEquals(setOf(component1, component2), graph.components())
    }

    @Test
    fun testCalculate() {
        val graph = ComponentDependencyGraph()
        val component1 = "1"
        val component2 = "2"
        val component3 = "3"
        graph.addDependency(component1, component2)
        graph.addDependency(component2, component3)
        val calculated = graph.calculate()
        assertEquals(mapOf(
            "1" to 3,
            "2" to 2,
            "3" to 1,
        ), calculated.componentAttributes.mapValues { it.value.componentDependencyValue })
        assertEquals(6, calculated.ccd)
        assertEquals(2.0, calculated.acd)
        assertEquals(0.66, calculated.pc, 0.01)
    }


    @Test
    fun testCalculateExampleGraph() {
        val graph = ComponentDependencyGraph()
        val component1 = "1"
        val component2 = "2"
        val component3 = "3"
        val component4 = "4"
        val component5 = "5"
        val component6 = "6"
        graph.addDependency(component1, component2)
        graph.addDependency(component2, component3)
        graph.addDependency(component2, component5)
        graph.addDependency(component2, component6)
        graph.addDependency(component4, component2)
        graph.addDependency(component4, component5)
        graph.addDependency(component5, component6)
        val calculated = graph.calculate()
        assertEquals(mapOf(
            "1" to 5,
            "2" to 4,
            "3" to 1,
            "4" to 5,
            "5" to 2,
            "6" to 1,
        ), calculated.componentAttributes.mapValues { it.value.componentDependencyValue })

        assertEquals(18, calculated.ccd)
        assertEquals(3.0, calculated.acd)
        assertEquals(0.5, calculated.pc)
    }
}