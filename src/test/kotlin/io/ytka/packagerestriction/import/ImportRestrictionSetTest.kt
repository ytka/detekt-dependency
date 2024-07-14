package io.ytka.packagerestriction.import

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionSetTest {
    @Test
    fun testIsAllowed() {
        val rule = ImportRestrictionSet(listOf(
            ImportRestrictionDeny("java.io", listOf("java.io.File")),
            ImportRestrictionAllow("java.io", listOf("java.io.FileInputStream"))
        ))
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.io.File"))
        assertFalse(rule.isAllowed("java.io", "java.net.URL"))
    }

    @Test
    fun testIsAllowedEmpty() {
        val rule = ImportRestrictionSet(listOf())
        assertTrue(rule.isAllowed("java.io", "java.io.FileInputStream"))
        assertTrue(rule.isAllowed("java.net", "java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.io.File"))
        assertTrue(rule.isAllowed("java.io", "java.net.URL"))
    }
}