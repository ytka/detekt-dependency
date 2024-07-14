package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import io.ytka.packagerestriction.imports.*
import org.jetbrains.kotlin.psi.KtImportList

class PackageRestrictionRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package restriction violation.",
        Debt.TWENTY_MINS)

    private val projectPackagePrefix = valueOrDefault("project-package-prefix", "")
    private val packageRestrictionSet = PackageRestrictions(projectPackagePrefix)
    //private val threshold = 10
    //private var amount: Int = 0

    init {
        val pkgs = valueOrNull<List<Map<String,Any>>>("packages")?: listOf()
        packageRestrictionSet.append(pkgs)
    }

    override fun visitImportList(importList: KtImportList) {
        super.visitImportList(importList)

        val sourcePkg = importList.containingKtFile.packageFqName.toString()
        val pkgRestrictions = packageRestrictionSet.findPackageRestrictions(sourcePkg)

        val destPkgs = importList.imports.map { importDirective -> importDirective.importPath?.pathStr ?: ""}
            .distinct()
        destPkgs.forEach { destPkg ->
            //println("sourcePkg: $sourcePkg, destPkg: $destPkg")
            pkgRestrictions.forEach { pkgRestriction ->
                if (!pkgRestriction.isAllowedImport(sourcePkg, destPkg)) {
                    report(CodeSmell(issue, Entity.from(importList),
                        "The import of $destPkg is not allowed from $sourcePkg. details: $pkgRestriction."))
                }
            }
        }
    }


    /*
    // TODO: calculate cyclomatic complexity
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        amount++
    }

     */
}
