package net.samystudio.permissionlauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Permission launcher for a set of permissions from a [Fragment].
 *
 * @See MultiplePermissionsLauncher
 */
class FragmentMultiplePermissionsLauncher(
    private val fragment: Fragment,
    contract: Contract,
) : MultiplePermissionsLauncher(contract) {
    override val launcher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            activityResultCallback
        )

    override fun hasPermission(permission : String) =
        fragment.hasPermission(permission)

    override fun hasAllPermissions() =
        fragment.hasAllPermissions(*contract.rawPermissions.toTypedArray())

    override fun hasAnyPermissions() =
        fragment.hasAnyPermissions(*contract.rawPermissions.toTypedArray())

    override fun shouldShowRequestPermissionRationales() =
        contract.permissions.filter { fragment.shouldShowRequestPermissionRationale(it) }.toSet()
}
