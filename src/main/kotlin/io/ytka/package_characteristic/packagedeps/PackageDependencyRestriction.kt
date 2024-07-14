package io.ytka.package_characteristic.packagedeps

interface PackageDependencyRestrictionRule {
    fun isAllowed(source: PackageName, dest: PackageName): Boolean
}

class PackageDependencyRestrictionRules(private val rules: List<PackageDependencyRestrictionRule>) : PackageDependencyRestrictionRule {
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        return rules.all { it.isAllowed(source, dest) }
    }
}


class PackageDependencyRestrictionRuleAllow(source: PackageName, allowedDests: List<PackageName>) : PackageDependencyRestrictionRule {
    private val sourceFilter = PackagePathFilter(source)
    private val allowPatterns: Set<PackagePathFilter> = allowedDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName) : Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return allowPatterns.any { it.matches(dest) }
    }
}

class PackageDependencyRestrictionRuleDeny(source: PackageName, denyDests: List<PackageName>) : PackageDependencyRestrictionRule {
    private val sourceFilter = PackagePathFilter(source)
    private val denyPatterns: Set<PackagePathFilter> = denyDests.map { PackagePathFilter(it) }.toSet()
    override fun isAllowed(source: PackageName, dest: PackageName): Boolean {
        if (!sourceFilter.matches(source)) {
            return true
        }
        return !denyPatterns.any { it.matches(dest) }
    }
}