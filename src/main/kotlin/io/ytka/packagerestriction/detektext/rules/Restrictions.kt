package io.ytka.packagerestriction.detektext.rules

import io.ytka.packagerestriction.imports.*
import io.ytka.packagerestriction.packagepath.PackagePathFilter

val ConfigProjectPackagePrefixKey = "project-package-prefix"
val ConfigRestrictionsKey = "restrictions"

class Restrictions<T> {
    private val restrictionMap: MutableMap<PackagePathFilter, T> = mutableMapOf()

    fun find(packagePath: PackagePath): List<T> {
        return restrictionMap.filter { it.key.matches(packagePath) }.values.toList()
    }
    fun add(pathFilter: PackagePathFilter, restriction: T) {
        restrictionMap[pathFilter] = restriction
    }
}
