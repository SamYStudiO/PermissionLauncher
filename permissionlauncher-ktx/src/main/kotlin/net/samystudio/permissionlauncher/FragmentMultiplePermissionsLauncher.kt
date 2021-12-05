package net.samystudio.permissionlauncher

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Permission launcher for a set of permissions from a [Fragment].
 *
 * @See ActivityResultContracts.RequestMultiplePermissions
 */
class FragmentMultiplePermissionsLauncher(
    private val fragment: Fragment,
    contract: Contract,
) : MultiplePermissionsLauncher(contract) {
    private val activityResultCallback =
        ActivityResultCallback<Map<String, Boolean>> { handleResult(it) }
    private val activityResultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        activityResultCallback
    )

    override fun hasPermission(permission: String) =
        fragment.hasPermission(permission)

    override fun hasAllPermissions() =
        fragment.hasAllPermissions(*contract.rawPermissions.toTypedArray())

    override fun hasAnyPermissions() =
        fragment.hasAnyPermissions(*contract.rawPermissions.toTypedArray())

    override fun shouldShowRequestPermissionRationales() =
        contract.permissions.filter { fragment.shouldShowRequestPermissionRationale(it) }.toSet()

    override fun launch() {
        activityResultLauncher.launch(contract.permissions.toTypedArray())
    }
}
