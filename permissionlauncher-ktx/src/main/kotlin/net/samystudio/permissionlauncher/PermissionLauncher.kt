@file:Suppress("unused")

package net.samystudio.permissionlauncher

/**
 * A permission launcher for a unique permission. For multiple permissions use
 * [MultiplePermissionsLauncher].
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
    private var deniedCallback: ((permission: String, neverAskAgain: Boolean) -> Unit)? = null
    private var grantedCallback: ((permission: String) -> Unit)? = null
    private val rationalePermissionLauncher by lazy {
        RationalePermissionLauncher(
            ::internalCancelled,
            ::internalDenied,
        ) { launch() }
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
        deniedCallback: ((permission: String, neverAskAgain: Boolean) -> Unit)? = null,
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
                launch()
        }
    }

    protected abstract fun hasPermission(): Boolean
    protected abstract fun shouldShowRequestPermissionRationale(): Boolean
    protected abstract fun launch()

    protected fun handleResult(granted: Boolean) {
        if (granted)
            internalGranted()
        else
            internalDenied()
    }

    private fun internalCancelled() {
        deniedCallback = null
        grantedCallback = null
    }

    private fun internalDenied() {
        deniedCallback?.invoke(permission, shouldShowRequestPermissionRationale())
        deniedCallback = null
        grantedCallback = null
    }

    private fun internalGranted() {
        grantedCallback?.invoke(permission)
        deniedCallback = null
        grantedCallback = null
    }

    interface Launcher {
        fun launch(permission: String)
    }
}
