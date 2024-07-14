package io.ytka.packagerestriction.detektext.rules

import io.ytka.packagerestriction.detektext.metrics.CyclomaticComplexMethod
import io.ytka.packagerestriction.imports.*

class PackageRestriction(
    val importRestriction: ImportRestrictionInProject,
    val cyclomaticComplexMethod: CyclomaticComplexMethod,
)

class PackageRestrictions(private val projectPackagePrefix: String) {
    fun findPackageRestrictions(packagePath: PackagePath): List<PackageRestriction> {
        if (!packagePath.startsWith(projectPackagePrefix)) {
            return emptyList()
        }
        return packageRestrictionSets.filter { it.key.matches(packagePath) }.values.toList()
    }

    fun append(definitions: List<Map<String,Any>>) {
        definitions.forEach() { pkg ->
            val name = pkg["name"]
            @Suppress("UNCHECKED_CAST")
            val importAllows = pkg["import-allows"] as List<String>?
            @Suppress("UNCHECKED_CAST")
            val importDenys = pkg["import-denys"] as List<String>?
            val cyclomaticComplexityThreshold = pkg["cyclomatic-complex-threshold"] as Int?
            // println("name: $name, importAllows: $importAllows, importDenys: $importDenys, cyclomaticComplexityThreshold: $cyclomaticComplexityThreshold")

            val importRestriction = createImportRestriction(name as String, importAllows, importDenys)
            this.addPackageRestriction(PackagePathFilter(name), importRestriction, CyclomaticComplexMethod(cyclomaticComplexityThreshold))
        }
    }

    private val packageRestrictionSets: MutableMap<PackagePathFilter, PackageRestriction> = mutableMapOf()
    private fun addPackageRestriction(pathFilter: PackagePathFilter, importRestriction: ImportRestriction, cyclomaticComplexMethod: CyclomaticComplexMethod) {
        packageRestrictionSets[pathFilter] = PackageRestriction(
            importRestriction = ImportRestrictionInProject(projectPackagePrefix, pathFilter, importRestriction),
            cyclomaticComplexMethod = cyclomaticComplexMethod,
        )
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