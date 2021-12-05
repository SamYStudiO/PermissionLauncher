package net.samystudio.permissionlauncher

import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class PermissionLauncherTest {

    @Test
    fun `permission is already granted`() {
        val testLauncher = object : PermissionLauncher("hello_permission") {
            override fun launch() {}
            override fun hasPermission() = true
            override fun shouldShowRequestPermissionRationale() = false
        }

        var permission = ""
        val rationale: (permission: String, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permission: String) -> Unit = {}
        val granted: (permission: String) -> Unit = { permission = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals("hello_permission", permission)
    }

    @Test
    fun `permission is granted now`() {
        val testLauncher = object : PermissionLauncher("hello_permission") {
            override fun launch() {
                handleResult(true)
            }

            override fun hasPermission() = false
            override fun shouldShowRequestPermissionRationale() = false
        }

        var permission = ""
        val rationale: (permission: String, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permission: String) -> Unit = {}
        val granted: (permission: String) -> Unit = { permission = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals("hello_permission", permission)
    }

    @Test
    fun `permission need rationale`() {
        val testLauncher = object : PermissionLauncher("hello_permission") {
            override fun launch() {}
            override fun hasPermission() = false
            override fun shouldShowRequestPermissionRationale() = true
        }

        var permission = ""
        val rationale: (permission: String, RationalePermissionLauncher) -> Unit =
            { perm, _ -> permission = perm }
        val denied: (permission: String) -> Unit = {}
        val granted: (permission: String) -> Unit = {}
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 1) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals("hello_permission", permission)
    }

    @Test
    fun `permission is denied`() {
        val testLauncher = object : PermissionLauncher("hello_permission") {
            override fun launch() {
                handleResult(false)
            }

            override fun hasPermission() = false
            override fun shouldShowRequestPermissionRationale() = false
        }

        var permission = ""
        val rationale: (permission: String, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permission: String) -> Unit = { permission = it }
        val granted: (permission: String) -> Unit = {}
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 1) { deniedSpy.invoke(any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals("hello_permission", permission)
    }
}
