@file:Suppress("unused")

package net.samystudio.permissionlauncher

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Convenient way to get if a [permission] is granted from a [Context].
 * You may specified [maxSdkVersion] for this permission, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result will be false.
 */
fun Context.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED ||
        permission.isUselessPermission()

/**
 * Convenient way to get if a set of permissions are granted from a [Context].
 * All [permissions] need to be granted to get this returns true.
 * You may specified [maxSdkVersion] for these permissions, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result will be false.
 */
fun Context.hasAllPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.none { !it }

/**
 * Convenient way to get if a set of permissions are granted from a [Context].
 * At least one of [permissions] need to be granted to get this returns true.
 * You may specified [maxSdkVersion] for these permissions, for example :
 * <code>Manifest.Permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P</code>
 * In this case users running P or later will be considered as [PackageManager.PERMISSION_GRANTED]
 * and result will be true.
 * If you omit [maxSdkVersion] and have specified maxSdkVersion from your manifest then Android will
 * consider this as [PackageManager.PERMISSION_DENIED] and result may be false.
 */
fun Context.hasAnyPermissions(vararg permissions: String) =
    permissions.map { hasPermission(it) }.any { it }
