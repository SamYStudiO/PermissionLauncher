@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

/**
 * Convenient way to get if a [permission] is granted from an [Activity].
 * You may specified [maxSdkVersion] for this permission, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result will be false.
 *
 */
fun Activity.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED ||
        permission.isUselessPermission()

/**
 * Convenient way to get if a set of permissions are granted from an [Activity].
 * All [permissions] need to be granted to get this returns true.
 * You may specified [maxSdkVersion] for these permissions, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result will be false.
 */
fun Activity.hasAllPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
 * Convenient way to get if a set of permissions are granted from a [Activity].
 * At least one of [permissions] need to be granted to get this returns true.
 * You may specified [maxSdkVersion] for these permissions, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result may be false.
 */
fun Activity.hasAnyPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.any { it }

/**
 * Convenient way to create a [PermissionLauncher] from current [ComponentActivity].
 *
 * @see [ActivityPermissionLauncher]
 */
fun ComponentActivity.createPermissionLauncher(permission: String): PermissionLauncher =
    ActivityPermissionLauncher(
        this,
        permission,
    )

/**
 * Convenient way to create a [MultiplePermissionsLauncher] from current [ComponentActivity].
 *
 * @see [ActivityMultiplePermissionsLauncher]
 */
fun ComponentActivity.createMultiplePermissionsLauncher(
    multiplePermissionsContract: MultiplePermissionsLauncher.Contract,
): MultiplePermissionsLauncher = ActivityMultiplePermissionsLauncher(
    this,
    multiplePermissionsContract,
)
