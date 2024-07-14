package io.ytka.package_characteristic.detektext.processors

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.FileProcessListener
import io.ytka.package_characteristic.domain.packagedeps.PackageDependencyStore
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.psi.KtFile

val DependenciesKey = Key<PackageDependencyStore>("DependenciesKey")

class DependencyProcessor : FileProcessListener {
    private var dependencyStore = PackageDependencyStore.of(emptyList(), emptyList())

    override fun init(config : Config) {
        val sub = config.subConfig("dependency").subConfig("PackageImport")
        val includes = sub.valueOrDefault("includes", emptyList<String>())
        val excludes = sub.valueOrDefault("excludes", emptyList<String>())
        dependencyStore = PackageDependencyStore.of(includes, excludes)
    }

    override fun onProcess(file: KtFile, bindingContext: org.jetbrains.kotlin.resolve.BindingContext) {
        file.importDirectives.forEach { importDirective ->
            dependencyStore.addDependency(file.packageFqName.asString(), importDirective.importedFqName?.parent()?.asString())
        }
    }

    override fun onFinish(files: List<KtFile>, result: io.gitlab.arturbosch.detekt.api.Detektion, bindingContext: org.jetbrains.kotlin.resolve.BindingContext) {
        result.addData(DependenciesKey, dependencyStore)
    }
}
