package io.ytka.packagerestriction.detektext.rules

import io.github.detekt.psi.absolutePath
import io.gitlab.arturbosch.detekt.api.*
import io.ytka.packagerestriction.import.*
import org.jetbrains.kotlin.psi.KtFile


private fun createImportRestriction(name: String, importAllows: Any?, importDenys: Any?): ImportRestriction {
    if (importAllows != null && importDenys != null) {
        throw IllegalArgumentException("Cannot have both import-allows and import-denys")
    }
    if (importAllows != null && importAllows is List<*>) {
        return ImportRestrictionAllow(name, importAllows as List<String>)
    }
    if (importDenys != null && importDenys is List<*>) {
        return ImportRestrictionDeny(name, importDenys as List<String>)
    }
    return ImportRestrictionDeny(name, emptyList())
}



class PackageRestrictionRule(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package restriction violation.",
        Debt.TWENTY_MINS)

    private val packagePrefix = valueOrDefault("package-prefix", "unknown")
    private val packageRestrictionSet = PackageRestrictionSet()
    //private val threshold = 10
    //private var amount: Int = 0

    init {
        val pkgs = valueOrNull<List<Map<String,Any>>>("packages")
        pkgs?.forEach() {
            val name = it["name"]
            val importAllows = it["import-allows"]
            val importDenys = it["import-denys"]
            println("name: $name, importAllows: $importAllows, importDenys: $importDenys")
            val importRestriction = createImportRestriction(name as String, importAllows, importDenys)
            packageRestrictionSet.addPackageRestriction(PackagePathFilter(name), importRestriction)
        }
    }

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)

        //println("----")
        //println(file.absolutePath())
        //println(file.importDirectives.map { it.importedFqName?.asString() })

        val sourcePkg = file.packageFqName.asString()
        val pkgRestrictions = packageRestrictionSet.findPackageRestrictions(sourcePkg)

        file.importDirectives.forEach { importDirective ->
//            val destPkg = importDirective.importedFqName?.parent()?.asString()
            val destPkg = importDirective.importedFqName.toString()
            println("sourcePkg: $sourcePkg, destPkg: $destPkg")

            pkgRestrictions.forEach {
                if (!it.importRestriction.isAllowed(sourcePkg, destPkg)) {
                    report(CodeSmell(issue, Entity.from(file),
                        "The import of $destPkg is not allowed in $sourcePkg."))
                }
            }
        }
        /*
        if (amount > threshold) {
            report(CodeSmell(issue, Entity.from(file),
                "The amount of functions in this file is higher than the threshold of $threshold."))
        }
        amount = 0

         */
    }

    /*
    // TODO: calculate cyclomatic complexity
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        amount++
    }

     */
}
