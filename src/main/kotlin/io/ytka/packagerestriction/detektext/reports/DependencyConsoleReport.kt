package io.ytka.packagerestriction.detektext.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.packagerestriction.detektext.metrics.componentdependency.CalculatedComponentDependency
import io.ytka.packagerestriction.detektext.processors.DependencyMetricsKey
import io.ytka.packagerestriction.packagedeps.PackageDependencyDiagram

class DependencyConsoleReport : ConsoleReport() {
    //override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
    override fun render(detektion: Detektion): String {
        // detektion.getData(DependenciesKey)?.toDiagram()
        val deps = detektion.getData(DependencyMetricsKey) ?: return ""
        //return deps.toString() + "\n" + toDiagram(deps)
        return deps.toString()
    }

    private fun toDiagram(deps: CalculatedComponentDependency): String {
            val sb = StringBuilder()
            sb.append("graph LR\n")
            deps.dependencies().forEach { (src, dest) ->
                sb.append("    $src --> $dest\n")
            }
            return sb.toString()

    }
}
