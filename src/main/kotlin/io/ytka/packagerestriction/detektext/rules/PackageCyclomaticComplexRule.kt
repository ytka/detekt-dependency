package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import io.ytka.packagerestriction.detektext.metrics.CyclomaticComplexMethod
import io.ytka.packagerestriction.packagepath.PackagePathFilter
import io.ytka.packagerestriction.packagepath.ProjectPackagePrefix
import org.jetbrains.kotlin.psi.KtNamedFunction

class PackageCyclomaticComplexRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package cyclomatic complexity violation.",
        Debt.TWENTY_MINS)

    private val projectPackagePrefix = ProjectPackagePrefix(valueOrDefault(ConfigProjectPackagePrefixKey, ""))
    private val restrictions = Restrictions<CyclomaticComplexMethod>()

    init {
        val definitions = valueOrNull<List<Map<String,Any>>>(ConfigRestrictionsKey)?: listOf()
        definitions.forEach() { def ->
            val pkgPathFilter = (def["package"] as String).let { projectPackagePrefix.complementPackagePath(it) }
            val cyclomaticComplexityThreshold = def["cyclomatic-complex-threshold"] as Int?
            restrictions.add(PackagePathFilter(pkgPathFilter), CyclomaticComplexMethod(cyclomaticComplexityThreshold))
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val sourcePkg = function.containingKtFile.packageFqName.toString()
        restrictions.find(sourcePkg).forEach { restriction ->
            restriction.visitNamedFunction(function)?.let { report(ThresholdedCodeSmell(issue, it.first, it.second, it.third)) }
        }
    }
}
