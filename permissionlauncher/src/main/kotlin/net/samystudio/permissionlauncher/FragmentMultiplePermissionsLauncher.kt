package net.samystudio.permissionlauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * @See MultiplePermissionsLauncher
 */
class FragmentMultiplePermissionsLauncher(
    private val fragment: Fragment,
    permissions: Set<String>,
    globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: ((Set<String>) -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
) : MultiplePermissionsLauncher(
    permissions,
    globalRationale,
    globalDenied,
    globalGranted,
) {
    override val launcher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            activityResultCallback
        )

    override fun shouldShowRequestPermissionRationales(): Set<String> =
        permissions.filter { fragment.shouldShowRequestPermissionRationale(it) }.toSet()

    override fun hasPermissions() =
        fragment.hasPermissions(*permissions.toTypedArray())
}
