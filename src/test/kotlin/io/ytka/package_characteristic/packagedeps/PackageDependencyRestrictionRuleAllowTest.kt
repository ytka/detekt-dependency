package io.ytka.package_characteristic.packagedeps

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PackageDependencyRestrictionRuleAllowTest {
    @Test
    fun testIsAllowed() {
        val rule = PackageDependencyRestrictionRuleAllow("java.io", listOf("java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.net", "java.io"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }
}