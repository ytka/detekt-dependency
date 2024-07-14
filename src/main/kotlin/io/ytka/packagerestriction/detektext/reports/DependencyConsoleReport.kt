package io.ytka.packagerestriction.detektext.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.packagerestriction.detektext.processors.DependenciesKey

class DependencyConsoleReport : ConsoleReport() {
    override fun render(detektion: Detektion) = ""
    //override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
}
