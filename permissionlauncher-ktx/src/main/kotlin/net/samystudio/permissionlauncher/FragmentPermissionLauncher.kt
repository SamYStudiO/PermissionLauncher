package net.samystudio.permissionlauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * @See PermissionLauncher
 */
class FragmentPermissionLauncher(
    private val fragment: Fragment,
    permission: String,
    maxSdkInt: Int? = null,
    globalRationale: ((RationalePermissionLauncher) -> Unit)? = null,
    globalDenied: (() -> Unit)? = null,
    globalGranted: (() -> Unit)? = null,
) : PermissionLauncher(
    permission,
    maxSdkInt,
    globalRationale,
    globalDenied,
    globalGranted,
) {
    override val launcher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            activityResultCallback
        )

    override fun shouldShowRequestPermissionRationale(): Boolean =
        fragment.shouldShowRequestPermissionRationale(permission)

    override fun hasPermission() =
        fragment.hasPermission(permission)
}
