@file:Suppress("unused")

package net.samystudio.permissionlauncher

class RationalePermissionLauncher(
    private val canceled: () -> Unit,
    private val denied: () -> Unit,
    private val accepted: () -> Unit,
) {
    /**
     * Cancel request without calling any callback.
     */
    fun cancel() {
        canceled()
    }

    /**
     * Deny request and call deny callback.
     */
    fun deny() {
        denied()
    }

    /**
     * Accept request and launch system permission dialog.
     */
    fun accept() {
        accepted()
    }
}
