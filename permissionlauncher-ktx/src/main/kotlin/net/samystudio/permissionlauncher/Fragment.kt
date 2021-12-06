@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * Convenient way to get if a [permission] is granted from a [Fragment].
 * @see Context.hasPermission
 */
fun Fragment.hasPermission(permission: String) =
    requireContext().hasPermission(permission)

/**
 * Convenient way to get if a set of permissions are granted from a [Fragment].
 * @see Context.hasAllPermissions
 */
fun Fragment.hasAllPermissions(vararg permissions: String) =
    requireContext().hasAllPermissions(*permissions)

/**
 * Convenient way to get if a set of permissions are granted from a [Fragment].
 * @see Context.hasAnyPermissions
 */
fun Fragment.hasAnyPermissions(vararg permissions: String) =
    requireContext().hasAnyPermissions(*permissions)

/**
 * Convenient way to create a [PermissionLauncher] from current [Fragment].
 *
 * @see [FragmentPermissionLauncher]
 */
fun Fragment.createPermissionLauncher(
    permission: String,
): PermissionLauncher = FragmentPermissionLauncher(
    this,
    permission,
)

/**
 * Convenient way to create a [MultiplePermissionsLauncher] from current [Fragment].
 *
 * @see [FragmentMultiplePermissionsLauncher]
 */
fun Fragment.createMultiplePermissionsLauncher(
    multiplePermissionsContract: MultiplePermissionsLauncher.Contract,
): MultiplePermissionsLauncher = FragmentMultiplePermissionsLauncher(
    this,
    multiplePermissionsContract,
)
