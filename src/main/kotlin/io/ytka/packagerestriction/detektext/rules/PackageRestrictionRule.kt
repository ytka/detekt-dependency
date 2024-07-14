package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import io.ytka.packagerestriction.import.*
import org.jetbrains.kotlin.psi.KtImportList


private fun createImportRestriction(name: String, importAllows: List<String>?, importDenys: List<String>?): ImportRestriction {
    if (importAllows != null && importDenys != null) {
        throw IllegalArgumentException("Cannot have both import-allows and import-denys")
    }
    if (importAllows != null) {
        return ImportRestrictionAllow(name, importAllows)
    }
    if (importDenys != null) {
        return ImportRestrictionDeny(name, importDenys)
    }
    return ImportRestrictionDeny(name, emptyList())
}



class PackageRestrictionRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package restriction violation.",
        Debt.TWENTY_MINS)

    private val projectPackagePrefix = valueOrDefault("project-package-prefix", "")
    private val packageRestrictionSet = PackageRestrictionSet(projectPackagePrefix)
    //private val threshold = 10
    //private var amount: Int = 0

    init {
        val pkgs = valueOrNull<List<Map<String,Any>>>("packages")
        pkgs?.forEach() {
            val name = it["name"]
            @Suppress("UNCHECKED_CAST")
            val importAllows = it["import-allows"] as List<String>?
            @Suppress("UNCHECKED_CAST")
            val importDenys = it["import-denys"] as List<String>?
            val importRestriction = createImportRestriction(name as String, importAllows, importDenys)
            packageRestrictionSet.addPackageRestriction(PackagePathFilter(name), importRestriction)
        }
    }

    override fun visitImportList(importList: KtImportList) {
        super.visitImportList(importList)

        val sourcePkg = importList.containingKtFile.packageFqName.toString()
        val pkgRestrictions = packageRestrictionSet.findPackageRestrictions(sourcePkg)

        val destPkgs = importList.imports.map { importDirective -> importDirective.importPath?.pathStr ?: ""}
            .map { importPath -> importPath.substringBeforeLast(".") }
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
