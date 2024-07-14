package io.ytka.detektrules.package_characteristic.reports

import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.ytka.detektrules.package_characteristic.processors.DependenciesKey

class DependencyConsoleReport : ConsoleReport() {
//    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toString() ?: ""
    override fun render(detektion: Detektion) = detektion.getData(DependenciesKey)?.toDiagram() ?: ""
}
