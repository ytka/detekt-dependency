package io.ytka.packagerestriction.detektext.rules

import io.ytka.packagerestriction.import.ImportRestriction
import io.ytka.packagerestriction.import.PackagePath
import io.ytka.packagerestriction.import.PackagePathFilter

class PackageRestriction(private val projectPackagePrefix: String, private val pathFilter: PackagePathFilter, private val importRestriction: ImportRestriction) {
    fun isAllowedImport(source: PackagePath, dest: PackagePath): Boolean {
        if (!source.startsWith(projectPackagePrefix) || !dest.startsWith(projectPackagePrefix)) {
            return true
        }
        /*
        if (pathFilter.matches(dest)) {
            return true
        }

         */
        return pathFilter.matches(source) && importRestriction.isAllowed(source, dest)
    }

    override fun toString(): String {
        return "PackageRestriction(projectPackagePrefix=$projectPackagePrefix, pathFilter=$pathFilter, importRestriction=$importRestriction)"
    }
}

class PackageRestrictionSet(private val projectPackagePrefix: String) {
    private val packageRestrictionSets: MutableMap<PackagePathFilter, PackageRestriction> = mutableMapOf()
    fun addPackageRestriction(pathFilter: PackagePathFilter, importRestriction: ImportRestriction) {
        packageRestrictionSets[pathFilter] = PackageRestriction(projectPackagePrefix, pathFilter, importRestriction)
    }
    fun findPackageRestrictions(packagePath: PackagePath): List<PackageRestriction> {
        if (!packagePath.startsWith(projectPackagePrefix)) {
            return emptyList()
        }
        return packageRestrictionSets.filter { it.key.matches(packagePath) }.values.toList()
    }
}