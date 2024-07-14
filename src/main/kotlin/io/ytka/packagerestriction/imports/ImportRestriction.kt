package io.ytka.packagerestriction.imports

import io.ytka.packagerestriction.packagepath.PackagePathFilter

typealias PackagePath = String

interface ImportRestriction {
    fun isAllowed(source: PackagePath, dest: PackagePath): Boolean

    fun isAllowOnProject(projectPackagePrefix: String, source: PackagePath, dest: PackagePath): Boolean {
        if (!dest.startsWith(projectPackagePrefix)) {
            return true
        }
        return isAllowed(source, dest)
    }
}

class ImportRestrictionSet(private val rules: List<ImportRestriction>) : ImportRestriction {
    override fun isAllowed(source: PackagePath, dest: PackagePath): Boolean {
        return rules.all { it.isAllowed(source, dest) }
    }
}

class ImportRestrictionAllow(source: PackagePath, allowedDests: List<PackagePath>) : ImportRestriction {
    private val sourceFilter = PackagePathFilter(source)
    private val allowPatterns: Set<PackagePathFilter> = allowedDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackagePath, dest: PackagePath) : Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return allowPatterns.any { it.matches(dest) }
    }
    override fun toString(): String {
        return "ImportRestrictionAllow(source=$sourceFilter, allowPatterns=$allowPatterns)"
    }
}

class ImportRestrictionDeny(source: PackagePath, denyDests: List<PackagePath>) : ImportRestriction {
    private val sourceFilter = PackagePathFilter(source)
    private val denyPatterns: Set<PackagePathFilter> = denyDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackagePath, dest: PackagePath): Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return !denyPatterns.any { it.matches(dest) }
    }
    override fun toString(): String {
        return "ImportRestrictionDeny(source=$sourceFilter, denyPatterns=$denyPatterns)"
    }
}