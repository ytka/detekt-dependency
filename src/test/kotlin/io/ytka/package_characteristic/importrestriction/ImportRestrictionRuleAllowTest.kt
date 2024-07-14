package io.ytka.package_characteristic.importrestriction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionRuleAllowTest {
    @Test
    fun testIsAllowed() {
        val rule = ImportRestrictionRuleAllow("java.io", listOf("java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.net", "java.io"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }
}