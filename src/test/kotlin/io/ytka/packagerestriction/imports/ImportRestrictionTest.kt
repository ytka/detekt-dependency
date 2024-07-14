package io.ytka.packagerestriction.imports

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImportRestrictionTest {
    @Test
    fun isAllowOnProject() {
        val importRestriction = ImportRestrictionAllow("io.myproject.app", listOf("io.myproject.lib"))

        assertTrue(importRestriction.isAllowOnProject("io.myproject", "io.myproject.app", "io.myproject.lib"))
        assertTrue(importRestriction.isAllowOnProject("io.myproject", "io.myproject.app", "otherproject.lib"))
        assertTrue(importRestriction.isAllowOnProject("io.myproject", "otherproject.app", "io.myproject.any"))

        assertFalse(importRestriction.isAllowOnProject("io.myproject",  "io.myproject.app", "io.myproject.any"))
    }
}