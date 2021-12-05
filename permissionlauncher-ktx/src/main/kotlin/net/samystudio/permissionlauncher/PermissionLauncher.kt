@file:Suppress("unused")

package net.samystudio.permissionlauncher

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * A permission launcher for a unique permission. For multiple permissions use
 * [MultiplePermissionsLauncher].
 *
 * @see ActivityResultContracts.RequestPermission
 */
abstract class PermissionLauncher(
    /**
     * Permission you want to request.
     * You may specified directly [maxSdkVersion] for this permission using the following :
     * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
     * For user running P or later launching permission request will always return permission as
     * granted.
     */
    protected val rawPermission: String,
) {
    private var deniedCallback: ((permission: String) -> Unit)? = null
    private var grantedCallback: ((permission: String) -> Unit)? = null
    protected abstract val launcher: ActivityResultLauncher<String>
    protected val activityResultCallback = ActivityResultCallback<Boolean> { granted ->
        if (granted)
            internalGranted()
        else
            internalDenied()
    }
    private val rationalePermissionLauncher by lazy {
        RationalePermissionLauncher(
            ::internalCancelled,
            ::internalDenied,
        ) { internalLaunch() }
    }

    /**
     * Requested permission.
     */
    val permission = rawPermission.normalizePermission()

    /**
     * Start permission request with optional specified callbacks.
     *
     * @param rationaleCallback A optional rationale callback called for this specific launch when this
     * launcher is launched and a rationale should be present to user.
     * @param deniedCallback A optional denied callback called for this specific launch when this launcher
     * failed.
     * @param grantedCallback A success callback called for this specific launch when this launcher
     * succeeded.
     */
    fun launch(
        rationaleCallback: ((permission: String, RationalePermissionLauncher) -> Unit)? = null,
        deniedCallback: ((permission: String) -> Unit)? = null,
        grantedCallback: (permission: String) -> Unit,
    ) {
        this.deniedCallback = deniedCallback
        this.grantedCallback = grantedCallback

        when {
            hasPermission() ->
                internalGranted()
            shouldShowRequestPermissionRationale() ->
                rationaleCallback?.invoke(permission, rationalePermissionLauncher)
            else ->
                internalLaunch()
        }
    }

    protected abstract fun hasPermission(): Boolean
    protected abstract fun shouldShowRequestPermissionRationale(): Boolean

    private fun internalLaunch() {
        launcher.launch(permission)
    }

    private fun internalCancelled() {
        deniedCallback = null
        grantedCallback = null
    }

    private fun internalDenied() {
        deniedCallback?.invoke(permission)
        deniedCallback = null
        grantedCallback = null
    }

    private fun internalGranted() {
        grantedCallback?.invoke(permission)
        deniedCallback = null
        grantedCallback = null
    }
}
