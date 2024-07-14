package io.ytka.packagerestriction.packagedeps

typealias PackageName = String
data class PackageDependency(val source: PackageName, val dest: PackageName)
