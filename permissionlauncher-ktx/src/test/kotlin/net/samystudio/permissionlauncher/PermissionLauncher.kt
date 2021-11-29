package net.samystudio.permissionlauncher

import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionLauncher {
    @Test
    fun `test permission set filtering with max sdk below`() {
        val permissions = setOf("a", "b", "c", "d")
        val maxSdks = setOf("b" to 28)
        val actual = permissions.filterNot { permission ->
            maxSdks.find { it.first == permission && 30 > it.second } != null
        }.toSet()
        val expected = setOf("a", "c", "d")

        assertEquals(expected, actual)
    }

    @Test
    fun `test permission set filtering with max sdk over`() {
        val permissions = setOf("a", "b", "c", "d")
        val maxSdks = setOf("b" to 28)
        val actual = permissions.filterNot { permission ->
            maxSdks.find { it.first == permission && 27 > it.second } != null
        }.toSet()
        val expected = setOf("a", "b", "c", "d")

        assertEquals(expected, actual)
    }
}
