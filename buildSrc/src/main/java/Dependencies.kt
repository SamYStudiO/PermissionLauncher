import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    // kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    // android
    const val core_ktx = "androidx.core:core-ktx:${Versions.core}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"

    // debug
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // test
    const val junit = "junit:junit:${Versions.junit}"
    const val test_core = "androidx.test:core:${Versions.test}"
    const val test_rules = "androidx.test:runner:${Versions.test}"
    const val test_runner = "androidx.test:runner:${Versions.test}"
    const val test_junit = "androidx.test.ext:junit:${Versions.test_junit}"
    const val test_truth = "androidx.test.ext:truth:${Versions.test}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val espresso_accessibility =
        "androidx.test.espresso:espresso-accessibility:${Versions.espresso}"
    const val espresso_web = "androidx.test.espresso:espresso-web:${Versions.espresso}"
    const val espresso_idling_concurrent =
        "androidx.test.espresso:idling:idling-concurrent:${Versions.espresso}"
    const val espresso_idling_resource =
        "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val mockito_core = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockito_android = "org.mockito:mockito-android:${Versions.mockito}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}

fun DependencyHandler.base() {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.core_ktx)
    implementation(Dependencies.activity)
    implementation(Dependencies.fragment)
}


fun DependencyHandler.debug() {
    implementation(Dependencies.timber)
}

fun DependencyHandler.test() {
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.mockito_core)
    testImplementation(Dependencies.mockk)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Dependencies.test_core)
    androidTestImplementation(Dependencies.test_runner)
    androidTestImplementation(Dependencies.test_rules)
    androidTestImplementation(Dependencies.test_junit)
    androidTestImplementation(Dependencies.test_truth)
    androidTestImplementation(Dependencies.espresso_core)
    //androidTestImplementation(Dependencies.espresso_contrib)
    //androidTestImplementation(Dependencies.espresso_intents)
    //androidTestImplementation(Dependencies.espresso_accessibility)
    //androidTestImplementation(Dependencies.espresso_web)
    //androidTestImplementation(Dependencies.espresso_idling_concurrent)
    //androidTestImplementation(Dependencies.espresso_idling_resource)
    androidTestImplementation(Dependencies.mockito_android)
    androidTestImplementation(Dependencies.mockk)
}

private fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

private fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}

private fun DependencyHandler.compileOnly(depName: String) {
    add("compileOnly", depName)
}

private fun DependencyHandler.debugImplementation(depName: String) {
    add("debugImplementation", depName)
}

private fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(depName: String) {
    add("androidTestImplementation", depName)
}

private fun DependencyHandler.kaptAndroidTest(depName: String) {
    add("kaptAndroidTest", depName)
}

private fun DependencyHandler.kaptTest(depName: String) {
    add("kaptTest", depName)
}
