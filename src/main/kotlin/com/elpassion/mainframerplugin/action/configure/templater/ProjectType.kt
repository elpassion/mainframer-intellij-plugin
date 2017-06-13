package com.elpassion.mainframerplugin.action.configure.templater

enum class ProjectType(val displayName: String,
                       val resourceDir: String) {
    ANDROID("Android", "gradle-android"),
    GRADLE("Gradle", "gradle"),
    GO("Go", "go"),
    GCC("Gcc", "gcc"),
    MVN("Mvn", "mvn"),
    RUST("Rust", "rust"),
    BUCK("Buck", "buck"),
    CLANG("Clang", "clang")
}