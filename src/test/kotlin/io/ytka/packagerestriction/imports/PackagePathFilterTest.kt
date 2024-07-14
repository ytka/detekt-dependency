package io.ytka.packagerestriction.imports

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PackagePathFilterTest {
    @Test
    fun testMatches() {
        val filter = PackagePathFilter("java.io")
        assertTrue(filter.matches("java.io"))

        assertFalse(filter.matches("java.io.File"))
        assertFalse(filter.matches("kotlin"))
        assertFalse(filter.matches("java.net"))
    }

    @Test
    fun testMatchesWithWildcard() {
        val filter = PackagePathFilter("java.io{,.*}")
        assertTrue(filter.matches("java.io"))
        assertTrue(filter.matches("java.io.File"))

        assertFalse(filter.matches("kotlin"))
        assertFalse(filter.matches("java.net"))
    }
}