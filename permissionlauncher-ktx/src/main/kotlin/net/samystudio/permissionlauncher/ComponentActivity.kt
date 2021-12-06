@file:Suppress("unused")

package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity

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
