package io.ytka.packagerestriction.imports

class ImportRestrictionInProject(private val projectPackagePrefix: String, private val pathFilter: PackagePathFilter, private val importRestriction: ImportRestriction) {
    fun isAllowedImport(source: PackagePath, dest: PackagePath): Boolean {
        //println("isAllowedImport: ${this.toString()}")
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
        return "ImportRestrictionInProject(projectPackagePrefix=$projectPackagePrefix, pathFilter=$pathFilter, importRestriction=$importRestriction)"
    }
}