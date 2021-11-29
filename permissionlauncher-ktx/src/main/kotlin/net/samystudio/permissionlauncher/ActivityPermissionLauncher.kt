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
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            activityResultCallback
        )

    override fun shouldShowRequestPermissionRationale(): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    override fun hasPermission() =
        activity.hasPermission(permission)
}
