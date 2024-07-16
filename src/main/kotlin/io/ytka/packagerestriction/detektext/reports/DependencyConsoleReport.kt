package io.ytka.packagerestriction.detektext.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.packagerestriction.detektext.processors.DependencyMetricsKey

class DependencyConsoleReport : ConsoleReport() {
    //override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
    override fun render(detektion: Detektion): String {
        // detektion.getData(DependenciesKey)?.toDiagram()
        return detektion.getData(DependencyMetricsKey).toString()
    }
}
