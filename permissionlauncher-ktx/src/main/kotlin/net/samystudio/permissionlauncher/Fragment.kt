@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Convenient way to get if a [permission] is granted from an [Fragment].
 */
fun Fragment.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

/**
 * Convenient way to get if a set of permissions are granted from a [Fragment].
 * All [permissions] need to be granted to get this returns true.
 */
fun Fragment.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
 * Convenient way to create a [PermissionLauncher] from current [Fragment].
 *
 * @see [FragmentPermissionLauncher]
 */
fun Fragment.createPermissionLauncher(
    permission: String,
    maxSdkInt: Int? = null,
    globalRationale: ((RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: (() -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
): PermissionLauncher = FragmentPermissionLauncher(
    this,
    permission,
    maxSdkInt,
    globalRationale,
    globalDenied,
    globalGranted,
)

/**
 * Convenient way to create a [MultiplePermissionsLauncher] from current [Fragment].
 *
 * @see [FragmentMultiplePermissionsLauncher]
 */
fun Fragment.createMultiplePermissionsLauncher(
    permissions: Set<String>,
    globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: ((Set<String>) -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
): MultiplePermissionsLauncher = FragmentMultiplePermissionsLauncher(
    this,
    permissions,
    globalRationale,
    globalDenied,
    globalGranted,
)
