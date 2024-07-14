package io.ytka.package_characteristic.packagedeps

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PackageDependencyRestrictionRuleDenyTest {
    @Test
    fun testIsAllowed() {
        val rule = PackageDependencyRestrictionRuleDeny("java.io", listOf("java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.io", "java.net.URL"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
    }
}