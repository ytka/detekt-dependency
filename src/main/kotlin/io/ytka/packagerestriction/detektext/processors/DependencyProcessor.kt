package io.ytka.packagerestriction.detektext.processors

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.FileProcessListener
import io.ytka.packagerestriction.detektext.metrics.componentdependency.CalculatedComponentDependency
import io.ytka.packagerestriction.detektext.metrics.componentdependency.ComponentDependencyGraph
import io.ytka.packagerestriction.packagedeps.PackageDependencyStore
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.psi.KtFile

val DependencyMetricsKey = Key<CalculatedComponentDependency>("DependencyMetrics")


class DependencyProcessor : FileProcessListener {
    private var projectPackagePrefix : String = ""
    private var dependencyGraph = ComponentDependencyGraph()

    override fun init(config : Config) {
        val sub = config.subConfig("package-restriction")
        projectPackagePrefix = sub.valueOrDefault("project-package-prefix", "")
    }

    override fun onProcess(file: KtFile, bindingContext: org.jetbrains.kotlin.resolve.BindingContext) {
        file.importDirectives.forEach { importDirective ->
            val dest = importDirective.importedFqName?.parent()?.asString()
            if (dest != null) {
                if (projectPackagePrefix.isEmpty() || dest.startsWith(projectPackagePrefix)) {
                    if (dest != projectPackagePrefix) {
                        dependencyGraph.addDependency(file.packageFqName.asString(), dest)
                    }
                }
            }
        }
    }

    override fun onFinish(files: List<KtFile>, result: io.gitlab.arturbosch.detekt.api.Detektion, bindingContext: org.jetbrains.kotlin.resolve.BindingContext) {
        result.addData(DependencyMetricsKey, dependencyGraph.calculate())
    }
}
