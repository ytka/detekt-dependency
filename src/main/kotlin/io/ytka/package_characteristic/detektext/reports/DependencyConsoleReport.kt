package io.ytka.package_characteristic.detektext.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.package_characteristic.detektext.processors.DependenciesKey

class DependencyConsoleReport : ConsoleReport() {
//    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toString() ?: ""
    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
}
