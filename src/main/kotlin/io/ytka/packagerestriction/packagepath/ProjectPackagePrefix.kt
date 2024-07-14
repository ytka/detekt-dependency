package io.ytka.packagerestriction.packagepath

@JvmInline
value class ProjectPackagePrefix(val value: String) {
    fun isProjectPackagePath(packagePath: String): Boolean {
        return packagePath.startsWith(value)
    }
    fun complementPackagePath(packagePath: String): String {
        if (isProjectPackagePath(packagePath)) {
            return packagePath
        }
        return "$value.$packagePath"
    }
}