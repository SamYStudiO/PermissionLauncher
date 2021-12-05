package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

/**
 * Permission launcher for a set of permissions from a [ComponentActivity].
 *
 * @See ActivityResultContracts.RequestMultiplePermissions
 */
class ActivityMultiplePermissionsLauncher(
    private val activity: ComponentActivity,
    contract: Contract,
) : MultiplePermissionsLauncher(contract) {
    private val activityResultCallback =
        ActivityResultCallback<Map<String, Boolean>> { handleResult(it) }
    private val activityResultLauncher = activity.registerForActivityResult(
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

    override fun launch() {
        activityResultLauncher.launch(contract.permissions.toTypedArray())
    }
}
