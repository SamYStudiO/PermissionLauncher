package net.samystudio.permissionlauncher

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import net.samystudio.permissionlauncher.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    // Permission launcher for Manifest.permission.READ_CONTACTS
    private val contactPermissionLauncher =
        createPermissionLauncher(Manifest.permission.READ_CONTACTS)

    // Permission launcher for any of Manifest.permission.ACCESS_COARSE_LOCATION and Manifest.permission.ACCESS_FINE_LOCATION
    private val locationPermissionLauncher = createMultiplePermissionsLauncher(
        anyOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    )

    // Permission launcher for all of Manifest.permission.CAMERA and Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val cameraPermissionLauncher = createMultiplePermissionsLauncher(
        allOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(
                LayoutInflater.from(this)
            ).also { setContentView(it.root) }

        binding.contactButton.setOnClickListener {
            contactPermissionLauncher.launch(
                rationaleCallback = { permission, rationalePermissionLauncher ->
                    showRationale(setOf(permission), rationalePermissionLauncher)
                    updateState()
                },
                deniedCallback = {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_CONTACTS
                        )
                    ) showDeniedInfo(setOf(Manifest.permission.READ_CONTACTS))
                    updateState()
                }
            ) {
                updateState()
            }
        }

        binding.locationButton.setOnClickListener {
            locationPermissionLauncher.launch(
                rationaleCallback = { permissions, rationalePermissionLauncher ->
                    showRationale(permissions, rationalePermissionLauncher)
                    updateState()
                },
                deniedCallback = { set ->
                    if (!set.map {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                it
                            )
                    }.contains(true)
                    ) showDeniedInfo(set)
                    updateState()
                }
            ) {
                updateState()
            }
        }

        binding.cameraButton.setOnClickListener {
            cameraPermissionLauncher.launch(
                rationaleCallback = { permissions, rationalePermissionLauncher ->
                    showRationale(permissions, rationalePermissionLauncher)
                    updateState()
                },
                deniedCallback = { set ->
                    if (!set.map {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                it
                            )
                    }.contains(true)
                    ) showDeniedInfo(set)
                    updateState()
                }
            ) {
                updateState()
            }
        }

        updateState()
    }

    private fun updateState() {
        binding.contactButton.text = Manifest.permission.READ_CONTACTS.split(".").last()
        binding.contactState.text = when {
            hasPermission(Manifest.permission.READ_CONTACTS) -> "permission granted"
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> "permission denied and should show rational"
            else -> "permission denied"
        }

        val sAny = "Any of " + Manifest.permission.ACCESS_COARSE_LOCATION.split(".")
            .last() + ", " + Manifest.permission.ACCESS_FINE_LOCATION.split(".").last()
        binding.locationButton.text = sAny
        binding.locationState.text = when {
            hasAnyPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> "permissions granted"
            setOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).map {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    it
                )
            }.contains(true) -> "permissions denied and should show rational"
            else -> "permissions denied"
        }

        val sAll = "All of " + Manifest.permission.CAMERA.split(".")
            .last() + ", " + Manifest.permission.WRITE_EXTERNAL_STORAGE.split(".").last()
        binding.cameraButton.text = sAll
        binding.cameraState.text = when {
            hasAllPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P
            ) -> "permissions granted"
            setOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).map {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    it
                )
            }.contains(true) -> "permissions denied and should show rational"
            else -> "permissions denied"
        }
    }

    private fun showRationale(
        permissions: Set<String>,
        rationalePermissionLauncher: RationalePermissionLauncher,
    ) {
        AlertDialog.Builder(this)
            .setTitle("Rationale")
            .setMessage(
                "Info about why user should grant permission to ${
                permissions.joinToString(" & ") { it.split(".").last() }
                }."
            )
            .setPositiveButton("Accept") { _, _ -> rationalePermissionLauncher.accept() }
            .setNegativeButton("Deny") { _, _ -> rationalePermissionLauncher.deny() }
            .setNeutralButton("Cancel") { _, _ -> rationalePermissionLauncher.cancel() }
            .create()
            .show()
    }

    private fun showDeniedInfo(permissions: Set<String>) {
        AlertDialog.Builder(this)
            .setTitle("Permission is denied")
            .setMessage(
                "Info about user has denied ${
                permissions.joinToString(" & ") { it.split(".").last() }
                } and need to go to app settings if he has changed his mind."
            )
            .setPositiveButton("Grant") { _, _ -> showAppSettings() }
            .setNegativeButton("Deny") { _, _ -> }
            .create()
            .show()
    }

    private fun showAppSettings() {
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts(
                    "package",
                    packageName,
                    null
                )
            )
        )
    }
}
