package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

/**
 * Permission launcher for a unique permission from a [ComponentActivity].
 *
 * @see ActivityResultContracts.RequestPermission
 */
class ActivityPermissionLauncher(
    private val activity: ComponentActivity,
    permission: String,
) : PermissionLauncher(permission) {
    private val activityResultCallback = ActivityResultCallback<Boolean> { handleResult(it) }
    private val activityResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        activityResultCallback
    )

    override fun hasPermission() =
        activity.hasPermission(rawPermission)

    override fun shouldShowRequestPermissionRationale(): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    override fun launch() {
        activityResultLauncher.launch(permission)
    }
}
