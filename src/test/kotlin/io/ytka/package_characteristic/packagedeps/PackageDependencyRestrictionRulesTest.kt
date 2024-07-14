package io.ytka.package_characteristic.packagedeps

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PackageDependencyRestrictionRulesTest {
    @Test
    fun testIsAllowed() {
        val rule = PackageDependencyRestrictionRules(listOf(
            PackageDependencyRestrictionRuleDeny("java.io", listOf("java.io.File")),
            PackageDependencyRestrictionRuleAllow("java.io", listOf("java.io.FileInputStream"))
        ))
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }
}