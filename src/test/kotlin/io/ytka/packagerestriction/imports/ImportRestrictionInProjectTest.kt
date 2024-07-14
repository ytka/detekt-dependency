package io.ytka.packagerestriction.imports

import org.junit.jupiter.api.Assertions.*

class ImportRestrictionInProjectTest {
    @org.junit.jupiter.api.Test
    fun isAllowedImport() {
        val importRestrictionInProject = ImportRestrictionInProject("io.myproject",
            PackagePathFilter("io.myproject.app"), ImportRestrictionAllow("io.myproject.app", listOf("io.myproject.lib")))

        assertTrue(importRestrictionInProject.isAllowedImport("io.myproject.app", "io.myproject.lib"))
        assertTrue(importRestrictionInProject.isAllowedImport("io.myproject.app", "otherproject.lib"))
        assertTrue(importRestrictionInProject.isAllowedImport("otherproject.app", "io.myproject.any"))

        assertFalse(importRestrictionInProject.isAllowedImport("io.myproject.app", "io.myproject.any"))
    }
}