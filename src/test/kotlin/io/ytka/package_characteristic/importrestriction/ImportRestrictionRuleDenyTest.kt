package io.ytka.package_characteristic.importrestriction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionRuleDenyTest {
    @Test
    fun testIsAllowed() {
        val rule = ImportRestrictionRuleDeny("java.io", listOf("java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.io", "java.net.URL"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
    }
}