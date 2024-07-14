package io.ytka.detektrules.package_characteristic.reports

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.OutputReport

class DependencyOutputReport : OutputReport() {
    override val ending: String = "md"
    override fun render(detektion: Detektion): String {
        println("hoge fuga")
        return "Dependency report"
    }
}
