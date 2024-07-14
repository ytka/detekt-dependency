package io.ytka.packagerestriction.detektext.rules

import io.ytka.packagerestriction.import.ImportRestriction
import io.ytka.packagerestriction.import.PackageName
import io.ytka.packagerestriction.import.PackagePathFilter

class PackageRestriction(val pathFilter: PackagePathFilter, val importRestriction: ImportRestriction) {
}

class PackageRestrictionSet {
    private val packageRestrictionSets: MutableMap<PackagePathFilter, PackageRestriction> = mutableMapOf()
    fun addPackageRestriction(pathFilter: PackagePathFilter, importRestriction: ImportRestriction) {
        packageRestrictionSets[pathFilter] = PackageRestriction(pathFilter, importRestriction)
    }
    fun findPackageRestrictions(packagePath: PackageName): List<PackageRestriction> {
        return packageRestrictionSets.filter { it.key.matches(packagePath) }.values.toList()
    }
}