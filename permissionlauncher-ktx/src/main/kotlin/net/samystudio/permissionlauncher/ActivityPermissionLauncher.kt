package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

/**
 * Permission launcher for a unique permission from a [ComponentActivity].
 *
 * @See PermissionLauncher
 */
class ActivityPermissionLauncher(
    private val activity: ComponentActivity,
    permission: String,
) : PermissionLauncher(permission) {
    override val launcher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            activityResultCallback
        )

    override fun hasPermission() =
        activity.hasPermission(rawPermission)

    override fun shouldShowRequestPermissionRationale(): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}
