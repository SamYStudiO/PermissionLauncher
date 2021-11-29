package net.samystudio.permissionlauncher

class RationalePermissionLauncher(
    private val accepted: () -> Unit,
    private val denied: () -> Unit
) {
    fun accept() {
        accepted()
    }

    fun deny() {
        denied()
    }
}