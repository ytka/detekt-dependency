package io.ytka.package_characteristic.importrestriction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionRuleSetTest {
    @Test
    fun testIsAllowed() {
        val rule = ImportRestrictionRuleSet(listOf(
            ImportRestrictionRuleDeny("java.io", listOf("java.io.File")),
            ImportRestrictionRuleAllow("java.io", listOf("java.io.FileInputStream"))
        ))
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }
}