package io.ytka.detekt.dependency.extension.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.detekt.dependency.extension.processors.DependenciesKey

class DependencyConsoleReport : ConsoleReport() {
//    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toString() ?: ""
    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
}
