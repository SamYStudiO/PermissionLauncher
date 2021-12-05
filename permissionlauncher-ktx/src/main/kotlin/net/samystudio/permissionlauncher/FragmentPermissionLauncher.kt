package net.samystudio.permissionlauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Permission launcher for a unique permission from a [Fragment].
 *
 * @See PermissionLauncher
 */
class FragmentPermissionLauncher(
    private val fragment: Fragment,
    permission: String,
) : PermissionLauncher(permission) {
    override val launcher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            activityResultCallback
        )

    override fun hasPermission() =
        fragment.hasPermission(rawPermission)

    override fun shouldShowRequestPermissionRationale() =
        fragment.shouldShowRequestPermissionRationale(permission)
}
