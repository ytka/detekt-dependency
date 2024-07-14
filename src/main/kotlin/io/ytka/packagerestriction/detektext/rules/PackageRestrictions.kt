package io.ytka.packagerestriction.detektext.rules

import io.ytka.packagerestriction.imports.*


class PackageRestrictions(private val projectPackagePrefix: String) {
    fun findPackageRestrictions(packagePath: PackagePath): List<ImportRestrictionInProject> {
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
            val importRestriction = createImportRestriction(name as String, importAllows, importDenys)
            this.addPackageRestriction(PackagePathFilter(name), importRestriction)
        }
    }

    private val packageRestrictionSets: MutableMap<PackagePathFilter, ImportRestrictionInProject> = mutableMapOf()
    private fun addPackageRestriction(pathFilter: PackagePathFilter, importRestriction: ImportRestriction) {
        packageRestrictionSets[pathFilter] = ImportRestrictionInProject(projectPackagePrefix, pathFilter, importRestriction)
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