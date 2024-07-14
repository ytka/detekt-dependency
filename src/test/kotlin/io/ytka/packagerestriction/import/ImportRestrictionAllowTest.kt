package io.ytka.packagerestriction.import

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionAllowTest {
    @Test
    fun testIsAllowed() {
        val rule = ImportRestrictionAllow("java.io", listOf("java.io.File"))

        assertTrue(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.net", "java.io"))

        assertFalse(rule.isAllowed("java.io", "java.io"))
        assertFalse(rule.isAllowed("java.io", "java.io.*"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }

    @Test
    fun testIsAllowedWithWildcard() {
        val rule = ImportRestrictionAllow("java.io", listOf("java.io.*"))

        assertTrue(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.Any"))
        assertTrue(rule.isAllowed("java.net", "java.io"))
        assertTrue(rule.isAllowed("java.io", "java.io.*"))

        assertFalse(rule.isAllowed("java.io", "java.io"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }
}