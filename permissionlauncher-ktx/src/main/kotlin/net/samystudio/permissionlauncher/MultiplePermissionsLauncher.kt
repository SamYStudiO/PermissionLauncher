@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.permissionlauncher

import androidx.activity.result.contract.ActivityResultContracts
import net.samystudio.permissionlauncher.MultiplePermissionsLauncher.Contract

/**
 * A permission launcher for multiples permissions.
 *
 * @see ActivityResultContracts.RequestMultiplePermissions
 */
abstract class MultiplePermissionsLauncher(
    /**
     * Permissions you want to request.
     * @see Contract
     */
    protected val contract: Contract,
) {
    private var deniedCallback: ((permissions: Set<String>, neverAskAgain: Boolean) -> Unit)? = null
    private var grantedCallback: ((permissions: Set<String>) -> Unit)? = null
    private var rationalePermissionLauncher: RationalePermissionLauncher? = null
    val permissions = contract.permissions

    /**
     * Start permission request with optional specified callbacks.
     *
     * @param rationaleCallback A optional rationale callback called for this specific launch when
     * this launcher is launched and a rationale should be present to user.
     * @param deniedCallback A optional denied callback called for this specific launch when this
     * launcher failed.
     * @param grantedCallback A success callback called for this specific launch when this launcher
     * succeeded.
     */
    fun launch(
        rationaleCallback: ((permissions: Set<String>, RationalePermissionLauncher) -> Unit)? = null,
        deniedCallback: ((permissions: Set<String>, neverAskAgain: Boolean) -> Unit)? = null,
        grantedCallback: (permissions: Set<String>) -> Unit,
    ) {
        this.deniedCallback = deniedCallback
        this.grantedCallback = grantedCallback

        val hasPermissions = when (contract) {
            is Contract.AllOf -> hasAllPermissions()
            is Contract.AnyOf -> hasAnyPermissions()
        }
        val rationales = shouldShowRequestPermissionRationales()

        when {
            hasPermissions ->
                internalGranted(
                    contract.rawPermissions.filter { hasPermission(it) }
                        .map { it.normalizePermission() }.toSet()
                )
            rationales.isNotEmpty() -> {
                rationalePermissionLauncher = RationalePermissionLauncher(
                    ::internalCancelled,
                    { internalDenied(rationales, false) },
                ) { internalLaunch() }
                rationaleCallback?.invoke(rationales, rationalePermissionLauncher!!)
            }
            else ->
                internalLaunch()
        }
    }

    protected abstract fun hasPermission(permission: String): Boolean
    protected abstract fun hasAllPermissions(): Boolean
    protected abstract fun hasAnyPermissions(): Boolean
    protected abstract fun shouldShowRequestPermissionRationales(): Set<String>
    protected abstract fun launch()

    protected fun handleResult(map: Map<String, Boolean>) {
        when (contract) {
            is Contract.AllOf ->
                if (hasAllPermissions())
                    internalGranted(
                        contract.rawPermissions.filter { hasPermission(it) }
                            .map { it.normalizePermission() }.toSet()
                    )
                else
                    internalDenied(
                        map.filter { !it.value }.keys.toSet(),
                        shouldShowRequestPermissionRationales().isEmpty()
                    )
            is Contract.AnyOf ->
                if (hasAnyPermissions())
                    internalGranted(
                        contract.rawPermissions.filter { hasPermission(it) }
                            .map { it.normalizePermission() }.toSet()
                    )
                else
                    internalDenied(
                        contract.permissions,
                        shouldShowRequestPermissionRationales().isEmpty()
                    )
        }
    }

    private fun internalLaunch() {
        launch()
    }

    private fun internalCancelled() {
        deniedCallback = null
        grantedCallback = null
        rationalePermissionLauncher = null
    }

    private fun internalDenied(permissions: Set<String>, neverAskAgain: Boolean) {
        deniedCallback?.invoke(permissions, neverAskAgain)
        deniedCallback = null
        grantedCallback = null
        rationalePermissionLauncher = null
    }

    private fun internalGranted(permissions: Set<String>) {
        grantedCallback?.invoke(permissions)
        deniedCallback = null
        grantedCallback = null
        rationalePermissionLauncher = null
    }

    /**
     * Internal usage or for Java users. Kotlin users should always use [allOf] or [anyOf] instead.
     */
    sealed class Contract(val rawPermissions: Set<String>) {
        val permissions = rawPermissions.map { it.normalizePermission() }.toSet()

        class AllOf(permissions: Collection<String>) : Contract(permissions.toSet())
        class AnyOf(permissions: Collection<String>) : Contract(permissions.toSet())
    }

    interface Launcher {
        fun launch(permissions: Set<String>)
    }
}

/**
 * Build a [Contract] with all of the specified permissions required.
 * @param permissions Permissions you want to request.
 * You may specified directly [maxSdkVersion] for these permissions using the following :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * For user running P or later launching permission request will always return permission as
 * granted.
 */
fun allOf(permissions: Collection<String>): Contract = Contract.AllOf(permissions)

/**
 * Build a [Contract] with all of the specified permissions required.
 * @param permissions Permissions you want to request.
 * You may specified directly [maxSdkVersion] for these permissions using the following :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * For user running P or later launching permission request will always return permission as
 * granted.
 */
fun allOf(vararg permissions: String): Contract = Contract.AllOf(permissions.toSet())

/**
 * Build a [Contract] with at least one of the specified permissions required.
 * @param permissions Permissions you want to request.
 * You may specified directly [maxSdkVersion] for these permissions using the following :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * For user running P or later launching permission request will always return permission as
 * granted.
 */
fun anyOf(permissions: Collection<String>): Contract = Contract.AnyOf(permissions)

/**
 * Build a [Contract] with at least one of the specified permissions required.
 * @param permissions Permissions you want to request.
 * You may specified directly [maxSdkVersion] for these permissions using the following :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * For user running P or later launching permission request will always return permission as
 * granted.
 */
fun anyOf(vararg permissions: String): Contract = Contract.AnyOf(permissions.toSet())
