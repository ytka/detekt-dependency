package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import io.ytka.packagerestriction.imports.*
import io.ytka.packagerestriction.packagepath.PackagePathFilter
import io.ytka.packagerestriction.packagepath.ProjectPackagePrefix
import io.ytka.packagerestriction.packagepath.complementPackagePathSuffix
import org.jetbrains.kotlin.psi.KtImportList

class PackageDependencyRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package dependency violation.",
        Debt.TWENTY_MINS)

    private val projectPackagePrefix = ProjectPackagePrefix(valueOrDefault(ConfigProjectPackagePrefixKey, ""))
    private val restrictions = Restrictions<ImportRestriction>()

    init {
        val definitions = valueOrNull<List<Map<String,Any>>>(ConfigRestrictionsKey)?: listOf()
        definitions.forEach() { def ->
            val pkgPathFilter = (def["package"] as String).let { projectPackagePrefix.complementPackagePath(it) }
            @Suppress("UNCHECKED_CAST")
            val importAllows = (def["import-allows"] as List<String>?)?.
                map { projectPackagePrefix.complementPackagePath(it) }?.
                map { complementPackagePathSuffix(it) }
            @Suppress("UNCHECKED_CAST")
            val importDenys = (def["import-denys"] as List<String>?)?.
                map { projectPackagePrefix.complementPackagePath(it) }?.
                map { complementPackagePathSuffix(it) }

            restrictions.add(PackagePathFilter(pkgPathFilter), createImportRestriction(pkgPathFilter, importAllows, importDenys))
        }
    }

    override fun visitImportList(importList: KtImportList) {
        super.visitImportList(importList)

        val sourcePkg = importList.containingKtFile.packageFqName.toString()
        if (!sourcePkg.startsWith(projectPackagePrefix.value)) {
            return
        }
        val restrictions = restrictions.find(sourcePkg)

        val destPkgs = importList.imports.map { importDirective -> importDirective.importPath?.pathStr ?: ""}
            .distinct()
        destPkgs.forEach { destPkg ->
            //println("sourcePkg: $sourcePkg, destPkg: $destPkg")
            restrictions.forEach { restriction ->
                if (!restriction.isAllowOnProject(projectPackagePrefix.value, sourcePkg, destPkg)) {
                    report(CodeSmell(issue, Entity.from(importList),
                        "The import of $destPkg is not allowed from $sourcePkg. details: $restriction."))
                }
            }
        }
    }
}


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