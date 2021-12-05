package net.samystudio.permissionlauncher

import android.Manifest
import android.os.Build

/**
 * A maximum Sdk version for a permission, for example requesting
 * [Manifest.permission.WRITE_EXTERNAL_STORAGE] may be useless after [Build.VERSION_CODES.P].
 */
infix fun String.maxSdkVersion(maxSdk: Int) =
    takeIf { Build.VERSION.SDK_INT <= maxSdk } ?: "$this$USE_LESS_SUFFIX"

internal fun String.isUselessPermission() = endsWith(USE_LESS_SUFFIX)
internal fun String.normalizePermission() = removeSuffix(USE_LESS_SUFFIX)

private const val USE_LESS_SUFFIX = ".useLess"
