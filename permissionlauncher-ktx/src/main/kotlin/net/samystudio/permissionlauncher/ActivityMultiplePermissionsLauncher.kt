package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

/**
 * Permission launcher for a set of permissions from a [ComponentActivity].
 *
 * @See MultiplePermissionsLauncher
 */
class ActivityMultiplePermissionsLauncher(
    private val activity: ComponentActivity,
    contract: Contract,
) : MultiplePermissionsLauncher(contract) {
    override val launcher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            activityResultCallback
        )

    override fun hasPermission(permission: String) =
        activity.hasPermission(permission)

    override fun hasAllPermissions() =
        activity.hasAllPermissions(*contract.rawPermissions.toTypedArray())

    override fun hasAnyPermissions() =
        activity.hasAnyPermissions(*contract.rawPermissions.toTypedArray())

    override fun shouldShowRequestPermissionRationales() =
        contract.permissions.filter {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                it
            )
        }.toSet()
}
