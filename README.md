Permission Launcher
===================
Android lightweight library to perform permissions request the right way using [ActivityResultCallback](https://developer.android.com/reference/androidx/activity/result/ActivityResultCallback), Check how to request permission from [documentation](https://developer.android.com/training/permissions/requesting#request-permission).

Usage
-----
**Unique permission request**
```kotlin
private val contactPermissionLauncher = createPermissionLauncher(Manifest.permission.READ_CONTACTS)
//...
button.setOnClickListener {
        contactPermissionLauncher.launch(
            rationaleCallback = { permission, rationalePermissionLauncher ->
                showRationale(permission, rationalePermissionLauncher)
                // From your rationale call either : 
                // + rationalePermissionLauncher.cancel() to cancel request
                // + rationalePermissionLauncher.deny() to call denied callback
                // + rationalePermissionLauncher.accept() to continue process and show Android dialog for permission
            },
            deniedCallback = { permission, neverAskAgain ->
                // handle denied
            }
        ) {
            // handle permission granted
        }
    }
````

**Multiple permissions with at least one permission required**
```kotlin
private val locationPermissionLauncher = createMultiplePermissionsLauncher(
        anyOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    )
//...
button.setOnClickListener {
        locationPermissionLauncher.launch(
            rationaleCallback = { permissions, rationalePermissionLauncher ->
                showRationale(permissions, rationalePermissionLauncher)
                // From your rationale call either : 
                // + rationalePermissionLauncher.cancel() to cancel request
                // + rationalePermissionLauncher.deny() to call denied callback
                // + rationalePermissionLauncher.accept() to continue process and show Android dialog for permissions
            },
            deniedCallback = { permissions, neverAskAgain ->
                // handle denied
            }
        ) {
           // handle permissions granted
        }
    }
````

**Multiple permissions with all permissions required and one required until Android SDK 28**
```kotlin
private val cameraPermissionLauncher = createMultiplePermissionsLauncher(
        allOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE maxSdkVersion Build.VERSION_CODES.P
        )
    )
//...
button.setOnClickListener {
        cameraPermissionLauncher.launch(
                rationaleCallback = { permissions, rationalePermissionLauncher ->
                    showRationale(permissions, rationalePermissionLauncher)
                    // From your rationale call either : 
                    // + rationalePermissionLauncher.cancel() to cancel request
                    // + rationalePermissionLauncher.deny() to call denied callback
                    // + rationalePermissionLauncher.accept() to continue process and show Android dialog for permissions
                },
                deniedCallback = { permissions, neverAskAgain ->
                    // handle denied
                }
            ) {
                // handle permissions granted
            }
    }
````

Check [sample](https://github.com/SamYStudiO/PermissionLauncher/tree/master/permissionlauncher-sample) for full usage.

Download
--------
```groovy
implementation 'net.samystudio.permissionlauncher:permissionlauncher-ktx:0.9.0'
```

Snapshots are available from [Sonatype's snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/).
If you want to run latest snapshot add its repository from your root `build.gradle`:
```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        //...
        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    }
}
```
and change versions:
```groovy
implementation 'net.samystudio.permissionlauncher:permissionlauncher-ktx:0.9.1-SNAPSHOT'
```

Publishing
-----

 1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
 2. Update the `CHANGELOG.md` for the impending release.
 3. Update the `README.md` with the new version.
 4. `git commit -am "Prepare for release X.Y.Z"` (where X.Y.Z is the new version)
 5. `./gradlew publish --no-daemon --no-parallel`
 6. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
 7. Update the `gradle.properties` to the next SNAPSHOT version.
 8. `git commit -am "Prepare next development version"`
 9. `git push && git push --tags`
 10. `./gradlew closeAndReleaseRepository` or visit [Sonatype Nexus](https://s01.oss.sonatype.org/) and promote the artifact.

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
