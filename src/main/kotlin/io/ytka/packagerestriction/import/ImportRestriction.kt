package io.ytka.packagerestriction.import

typealias PackageName = String

interface ImportRestriction {
    fun isAllowed(source: PackageName, dest: PackageName): Boolean
}

class ImportRestrictionSet(private val rules: List<ImportRestriction>) : ImportRestriction {
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        return rules.all { it.isAllowed(source, dest) }
    }
}

class ImportRestrictionAllow(source: PackageName, allowedDests: List<PackageName>) : ImportRestriction {
    private val sourceFilter = PackagePathFilter(source)
    private val allowPatterns: Set<PackagePathFilter> = allowedDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName) : Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return allowPatterns.any { it.matches(dest) }
    }
}

class ImportRestrictionDeny(source: PackageName, denyDests: List<PackageName>) : ImportRestriction {
    private val sourceFilter = PackagePathFilter(source)
    private val denyPatterns: Set<PackagePathFilter> = denyDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return !denyPatterns.any { it.matches(dest) }
    }
}