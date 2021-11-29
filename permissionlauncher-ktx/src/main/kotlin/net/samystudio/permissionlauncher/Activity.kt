@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Convenient way to get if a [permission] is granted from an [Activity].
 */
fun Activity.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * Convenient way to get if a set of permissions are granted from an [Activity].
 * All [permissions] need to be granted to get this returns true.
 */
fun Activity.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
 * Convenient way to create a [PermissionLauncher] from current [ComponentActivity].
 *
 * @see [ActivityPermissionLauncher]
 */
fun ComponentActivity.createPermissionLauncher(
    permission: String,
    maxSdkInt: Int? = null,
    globalRationale: ((RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: (() -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
): PermissionLauncher = ActivityPermissionLauncher(
    this,
    permission,
    maxSdkInt,
    globalRationale,
    globalDenied,
    globalGranted,
)

/**
 * Convenient way to create a [MultiplePermissionsLauncher] from current [ComponentActivity].
 *
 * @see [ActivityMultiplePermissionsLauncher]
 */
fun ComponentActivity.createMultiplePermissionsLauncher(
    permissions: Set<String>,
    maxSdks: Set<Pair<String,Int>>? = null,
    globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: ((permissions: Set<String>) -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
): MultiplePermissionsLauncher = ActivityMultiplePermissionsLauncher(
    this,
    permissions,
    maxSdks,
    globalRationale,
    globalDenied,
    globalGranted,
)
