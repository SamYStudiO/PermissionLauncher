@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Create a permission launcher for a unique permission. For multiple permissions use
 * [MultiplePermissionsLauncher].
 *
 * @see ActivityResultContracts.RequestPermission
 */
abstract class PermissionLauncher(
    val permission: String,
    /**
     * A maximum Sdk version this permission should be launched, for example asking
     * [Manifest.permission.WRITE_EXTERNAL_STORAGE] may be useless after [Build.VERSION_CODES.P].
     */
    private val maxSdkInt: Int? = null,
    /**
     * A optional rationale callback called everytime this launcher is launched and a rationale
     * should be present to user.
     */
    private val globalRationale: ((RationalePermissionLauncher) -> Unit)? = null,
    /**
     * A optional denied callback called everytime this launcher is launched and failed.
     */
    private val globalDenied: (() -> Unit)? = null,
    /**
     * A optional success callback called everytime this launcher is launched and succeeded.
     */
    private val globalSuccess: (() -> Unit)? = null,
) {
    private var localRationale: ((RationalePermissionLauncher) -> Boolean)? = null
    private var localDenied: (() -> Boolean)? = null
    private var localGranted: (() -> Boolean)? = null
    protected abstract val launcher: ActivityResultLauncher<String>
    protected val activityResultCallback = ActivityResultCallback<Boolean> { granted ->
        if (granted)
            internalGranted()
        else
            internalDenied()

    }
    private val rationalePermissionLauncher = RationalePermissionLauncher(
        ::internalLaunch,
        ::internalDenied,
    )

    /**
     * Start permission request with optional specified callbacks.
     *
     * @param rationale A optional rationale callback called for this specific launch when this
     * launcher is launched and a rationale should be present to user. Return true to call global
     * listener as well ([globalRationale]) or false to ignore global listener.
     * @param denied A optional denied callback called for this specific launch when this launcher
     * failed. Return true to call global listener as well ([globalDenied]) or false to ignore
     * global listener.
     * @param granted A optional success callback called for this specific launch when this launcher
     * succeeded. Return true to call global listener as well ([globalSuccess]) or false to ignore
     * global listener.
     */
    fun launch(
        rationale: ((RationalePermissionLauncher) -> Boolean)? = null,
        denied: (() -> Boolean)? = null,
        granted: (() -> Boolean)? = null,
    ) {
        this.localRationale = rationale
        this.localDenied = denied
        this.localGranted = granted

        when {
            hasPermission() || (maxSdkInt != null && Build.VERSION.SDK_INT > maxSdkInt) ->
                internalGranted()
            shouldShowRequestPermissionRationale() ->
                internalRational()
            else ->
                internalLaunch()
        }
    }

    protected abstract fun shouldShowRequestPermissionRationale(): Boolean
    protected abstract fun hasPermission(): Boolean

    private fun internalLaunch() {
        launcher.launch(permission)
    }

    private fun internalRational() {
        localRationale?.invoke(rationalePermissionLauncher).let {
            if (it != false)
                globalRationale?.invoke(rationalePermissionLauncher)
        }
    }

    private fun internalDenied() {
        localDenied?.invoke().let {
            if (it != false)
                globalDenied?.invoke()
        }
    }

    private fun internalGranted() {
        localGranted?.invoke().let {
            if (it != false)
                globalSuccess?.invoke()
        }
    }
}
