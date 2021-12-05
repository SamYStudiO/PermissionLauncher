package net.samystudio.permissionlauncher

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Permission launcher for a unique permission from a [Fragment].
 *
 * @see ActivityResultContracts.RequestPermission
 */
class FragmentPermissionLauncher(
    private val fragment: Fragment,
    permission: String,
) : PermissionLauncher(permission) {
    private val activityResultCallback = ActivityResultCallback<Boolean> { handleResult(it) }
    private val activityResultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        activityResultCallback
    )

    override fun hasPermission() =
        fragment.hasPermission(rawPermission)

    override fun shouldShowRequestPermissionRationale() =
        fragment.shouldShowRequestPermissionRationale(permission)

    override fun launch() {
        activityResultLauncher.launch(permission)
    }
}
