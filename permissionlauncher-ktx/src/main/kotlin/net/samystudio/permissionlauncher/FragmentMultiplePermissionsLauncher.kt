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
    permissions: Set<String>,
    maxSdks: Set<Pair<String,Int>>? = null,
    globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: ((Set<String>) -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
) : MultiplePermissionsLauncher(
    permissions,
    maxSdks,
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
        fragment.hasPermissions(*requiredPermission.toTypedArray())
}
