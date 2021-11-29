@file:Suppress("unused")

package net.samystudio.permissionlauncher

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Create a permission launcher for multiples permissions.
 *
 * @see ActivityResultContracts.RequestMultiplePermissions
 */
abstract class MultiplePermissionsLauncher(
    val permissions: Set<String>,
    /**
     * A optional rationale callback called everytime this launcher is launched and a rationale
     * should be present to user.
     */
    private val globalRationale: ((Set<String>, RationalePermissionLauncher) -> Unit)? = null,
    /**
     * A optional denied callback called everytime this launcher is launched and failed.
     */
    private val globalDenied: ((Set<String>) -> Unit)? = null,
    /**
     * A optional success callback called everytime this launcher is launched and succeeded.
     */
    private val globalGranted: (() -> Unit)? = null,
) {
    private var localRationale: ((Set<String>, RationalePermissionLauncher) -> Boolean)? =
        null
    private var localDenied: ((Set<String>) -> Boolean)? = null
    private var localGranted: (() -> Boolean)? = null
    protected abstract val launcher: ActivityResultLauncher<Array<String>>
    protected val activityResultCallback = ActivityResultCallback<Map<String, Boolean>> { map ->
        if (map.filter { !it.value }.isEmpty()) {
            internalGranted()
        } else
            map.filter { !it.value }.map { it.key }.toSet().let { set ->
                if (set.isNotEmpty())
                    internalDenied(set)
            }
    }

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
     * succeeded. Return true to call global listener as well ([globalGranted]) or false to ignore
     * global listener.
     */
    fun launch(
        rationale: ((Set<String>, RationalePermissionLauncher) -> Boolean)? = null,
        denied: ((Set<String>) -> Boolean)? = null,
        granted: (() -> Boolean)? = null,
    ) {
        this.localRationale = rationale
        this.localDenied = denied
        this.localGranted = granted

        val rationales = shouldShowRequestPermissionRationales()

        when {
            hasPermissions() ->
                internalGranted()
            rationales.isNotEmpty() ->
                internalRational(rationales)
            else ->
                internalLaunch()
        }
    }

    protected abstract fun shouldShowRequestPermissionRationales(): Set<String>
    protected abstract fun hasPermissions(): Boolean

    private fun internalLaunch() {
        launcher.launch(permissions.toTypedArray())
    }

    private fun internalRational(permissions: Set<String>) {
        val rationaleMultiplePermissionsLauncher = RationalePermissionLauncher(
            ::internalLaunch
        ) { internalDenied(permissions) }

        localRationale?.invoke(permissions, rationaleMultiplePermissionsLauncher).let {
            if (it != false)
                globalRationale?.invoke(permissions, rationaleMultiplePermissionsLauncher)
        }
    }

    private fun internalDenied(permissions: Set<String>) {
        localDenied?.invoke(permissions).let {
            if (it != false)
                globalDenied?.invoke(permissions)
        }
    }

    private fun internalGranted() {
        localGranted?.invoke().let {
            if (it != false)
                globalGranted?.invoke()
        }
    }
}