package io.ytka.package_characteristic.importrestriction

typealias PackageName = String

interface ImportRestrictionRule {
    fun isAllowed(source: PackageName, dest: PackageName): Boolean
}

class ImportRestrictionRuleSet(private val rules: List<ImportRestrictionRule>) : ImportRestrictionRule {
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        return rules.all { it.isAllowed(source, dest) }
    }
}


class ImportRestrictionRuleAllow(source: PackageName, allowedDests: List<PackageName>) : ImportRestrictionRule {
    private val sourceFilter = PackagePathFilter(source)
    private val allowPatterns: Set<PackagePathFilter> = allowedDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName) : Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return allowPatterns.any { it.matches(dest) }
    }
}

class ImportRestrictionRuleDeny(source: PackageName, denyDests: List<PackageName>) : ImportRestrictionRule {
    private val sourceFilter = PackagePathFilter(source)
    private val denyPatterns: Set<PackagePathFilter> = denyDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return !denyPatterns.any { it.matches(dest) }
    }
}