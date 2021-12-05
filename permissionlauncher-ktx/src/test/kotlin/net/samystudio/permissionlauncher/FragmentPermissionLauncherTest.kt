package net.samystudio.permissionlauncher

import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class FragmentPermissionLauncherTest {

    // All of --------------------------------------------------------------------------------------
    @Test
    fun `all permissions are already granted`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(allOf("hello_permission", "world_permission")) {
                override fun launch() {}
                override fun hasPermission(permission: String) = true
                override fun hasAllPermissions() = true
                override fun hasAnyPermissions() = true
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission", "world_permission"), permissions)
    }

    @Test
    fun `all permissions are granted`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(allOf("hello_permission", "world_permission")) {
                private var hasPermissions = false
                override fun launch() {
                    hasPermissions = true
                    handleResult(
                        mapOf(
                            "hello_permission" to true,
                            "world_permission" to true
                        )
                    )
                }

                override fun hasPermission(permission: String) = hasPermissions
                override fun hasAllPermissions() = hasPermissions
                override fun hasAnyPermissions() = hasPermissions
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission", "world_permission"), permissions)
    }

    @Test
    fun `all permissions but some need rationale`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(allOf("hello_permission", "world_permission")) {
                override fun launch() {}
                override fun hasPermission(permission: String) = false
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = false
                override fun shouldShowRequestPermissionRationales() = setOf("hello_permission")
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit =
            { perms, _ ->
                permissions = perms
            }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = {}
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 1) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `all permissions but some are denied`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(allOf("hello_permission", "world_permission")) {
                override fun launch() {
                    handleResult(
                        mapOf(
                            "hello_permission" to false,
                            "world_permission" to true
                        )
                    )
                }

                override fun hasPermission(permission: String) = false
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = false
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit =
            { perms, _ -> permissions = perms }
        val granted: (permissions: Set<String>) -> Unit = {}
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 1) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    // Any of --------------------------------------------------------------------------------------
    @Test
    fun `any of permissions are already granted`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                override fun launch() {}
                override fun hasPermission(permission: String) = permission == "hello_permission"
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = true
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `any of permissions are granted`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                private var hasPermissions = false
                override fun launch() {
                    hasPermissions = true
                    handleResult(
                        mapOf(
                            "hello_permission" to true,
                            "world_permission" to false
                        )
                    )
                }

                override fun hasPermission(permission: String) = permission == "hello_permission"
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = hasPermissions
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `any of permissions but some need rationale`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                override fun launch() {}
                override fun hasPermission(permission: String) = false
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = false
                override fun shouldShowRequestPermissionRationales() = setOf("hello_permission")
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit =
            { perms, _ ->
                permissions = perms
            }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = {}
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 1) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `any of permissions but some need rationale and one is already granted`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                override fun launch() {}
                override fun hasPermission(permission: String) = permission == "hello_permission"
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = true
                override fun shouldShowRequestPermissionRationales() = setOf("world_permission")
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `any of permissions but some are denied`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                var granted: Boolean = false
                override fun launch() {
                    granted = true
                    handleResult(
                        mapOf(
                            "hello_permission" to true,
                            "world_permission" to false
                        )
                    )
                }

                override fun hasPermission(permission: String) =
                    if (!granted) false else permission == "hello_permission"

                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = granted
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit = { _, _ -> }
        val granted: (permissions: Set<String>) -> Unit = { permissions = it }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 0) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 1) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission"), permissions)
    }

    @Test
    fun `any of permissions and all are denied`() {
        val testLauncher =
            object : MultiplePermissionsLauncher(anyOf("hello_permission", "world_permission")) {
                override fun launch() {
                    handleResult(
                        mapOf(
                            "hello_permission" to false,
                            "world_permission" to false
                        )
                    )
                }

                override fun hasPermission(permission: String) = false
                override fun hasAllPermissions() = false
                override fun hasAnyPermissions() = false
                override fun shouldShowRequestPermissionRationales() = emptySet<String>()
            }

        var permissions: Set<String> = emptySet()
        val rationale: (permissions: Set<String>, RationalePermissionLauncher) -> Unit = { _, _ -> }
        val denied: (permissions: Set<String>, neverAskAgain: Boolean) -> Unit =
            { perms, _ -> permissions = perms }
        val granted: (permissions: Set<String>) -> Unit = { }
        val rationaleSpy = spyk(rationale)
        val deniedSpy = spyk(denied)
        val grantedSpy = spyk(granted)
        testLauncher.launch(rationaleSpy, deniedSpy, grantedSpy)

        verify(exactly = 0) { rationaleSpy.invoke(any(), any()) }
        verify(exactly = 1) { deniedSpy.invoke(any(), any()) }
        verify(exactly = 0) { grantedSpy.invoke(any()) }
        Assert.assertEquals(setOf("hello_permission", "world_permission"), permissions)
    }
}
