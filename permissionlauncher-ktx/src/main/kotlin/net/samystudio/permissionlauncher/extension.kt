@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

fun Fragment.hasPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
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
 * @see [ActivityMultiplePermissionsLauncher]
 */
fun ComponentActivity.createMultiplePermissionsLauncher(
    permissions: Set<String>,
    globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: ((permissions: Set<String>) -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
): MultiplePermissionsLauncher = ActivityMultiplePermissionsLauncher(
    this,
    permissions,
    globalRationale,
    globalDenied,
    globalGranted,
)

/**
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
