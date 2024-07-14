package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtNamedFunction

class PackageRestrictionRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package restriction violation.",
        Debt.TWENTY_MINS)

    private val projectPackagePrefix = valueOrDefault("project-package-prefix", "")
    private val packageRestrictions = PackageRestrictions(projectPackagePrefix)

    init {
        val pkgs = valueOrNull<List<Map<String,Any>>>("restrictions")?: listOf()
        packageRestrictions.append(pkgs)
    }

    override fun visitImportList(importList: KtImportList) {
        super.visitImportList(importList)

        val sourcePkg = importList.containingKtFile.packageFqName.toString()
        val pkgRestrictions = packageRestrictions.findPackageRestrictions(sourcePkg)

        val destPkgs = importList.imports.map { importDirective -> importDirective.importPath?.pathStr ?: ""}
            .distinct()
        destPkgs.forEach { destPkg ->
            //println("sourcePkg: $sourcePkg, destPkg: $destPkg")
            pkgRestrictions.forEach { pkgRestriction ->
                if (!pkgRestriction.importRestriction.isAllowedImport(sourcePkg, destPkg)) {
                    report(CodeSmell(issue, Entity.from(importList),
                        "The import of $destPkg is not allowed from $sourcePkg. details: $pkgRestriction."))
                }
            }
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val sourcePkg = function.containingKtFile.packageFqName.toString()
        val pkgRestrictions = packageRestrictions.findPackageRestrictions(sourcePkg)

        pkgRestrictions.forEach { pkgRestriction ->
            pkgRestriction.cyclomaticComplexMethod.visitNamedFunction(function)?.let { report(it) }
        }
    }
}
