package net.samystudio.permissionlauncher

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

/**
 * Permission launcher for a set of permissions from a [ComponentActivity].
 *
 * @See MultiplePermissionsLauncher
 */
class ActivityMultiplePermissionsLauncher(
    private val activity: ComponentActivity,
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
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            activityResultCallback
        )

    override fun shouldShowRequestPermissionRationales(): Set<String> =
        permissions.filter {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }.toSet()

    override fun hasPermissions() =
        activity.hasPermissions(*permissions.toTypedArray())
}
