package io.ytka.package_characteristic.packagedeps

typealias PackageName = String
data class PackageDependency(val source: PackageName, val dest: PackageName)
