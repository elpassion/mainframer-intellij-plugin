import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import java.util.regex.Pattern

buildscript {
    repositories {
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
}

plugins {
    jacoco
    id("org.jetbrains.intellij") version "0.4.16"
    kotlin("jvm") version "1.3.70"
}
val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

group = "com.elpassion.mainframerplugin"
version = readVersion()

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].java {
        srcDir("src/main/kotlin")
    }
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
    val check by tasks
    check.dependsOn(this)
}

repositories {
    jcenter()
}

intellij {
    version = "IC-2019.1"
    pluginName = "mainframer-integration"
    updateSinceUntilBuild = true
}

val publishPlugin: PublishTask by tasks
val patchPluginXml: PatchPluginXmlTask by tasks

publishPlugin {
    username(project.findProperty("MF_PUBLISH_USER_NAME") as String?)
    password(project.findProperty("MF_PUBLISH_PASSWORD") as String?)
    channels(listOf(project.findProperty("publishChannel") as String?))
}

patchPluginXml {
    sinceBuild("145")
    untilBuild("191.*")
}

fun readVersion(): String {
    val versionFile = File("version.gradle.properties")
    val matcher = Pattern.compile("version=(\\d+.\\d+.\\d+)").matcher(versionFile.readText())
    if (matcher.find()) {
        return matcher.group(1)
    } else {
        throw RuntimeException("Version not found!")
    }
}

val versionName: String by project
task("updateVersion") {
    doLast {
        val versionFile = File("version.gradle.properties")
        val matcher = Pattern.compile("version=(\\d+.\\d+.\\d+)").matcher(versionFile.readText())
        if (matcher.find()) {
            val versionText =
                StringBuilder(versionFile.readText()).replace(matcher.start(1), matcher.end(1), versionName.toString())
            versionFile.writeText(versionText.toString())
        }
    }
}

dependencies {
    compile(kotlin("stdlib", kotlinVersion))
    compile(kotlin("reflect", kotlinVersion))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
    compile("com.fasterxml.jackson.core:jackson-databind:2.8.6")
    compile("com.squareup.retrofit2:retrofit:2.1.0")
    compile("com.squareup.retrofit2:converter-jackson:2.1.0")
    compile("io.reactivex.rxjava2:rxjava:2.0.5")
    compile("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")

    testCompile("com.nhaarman:mockito-kotlin:1.2.0")
    testCompile("com.squareup.okhttp3:mockwebserver:3.6.0")
    testCompile("junit:junit:4.11")
    testCompile("org.assertj:assertj-core:3.6.1")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
