import org.gradle.api.tasks.SourceSet
import java.util.regex.Pattern
import org.gradle.script.lang.kotlin.*
import org.jetbrains.intellij.*

buildscript {
    repositories {
        gradleScriptKotlin()
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

plugins {
    id("org.jetbrains.intellij") version "0.2.11"
    id("org.jetbrains.kotlin.jvm") version "1.1.1"
}

group = "com.elpassion.mainframerplugin"
version = readVersion()

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].java {
        srcDir("src/main/kotlin")
    }
}

repositories {
    jcenter()
}

configure<IntelliJPluginExtension> {
    version = "IC-171.4424.56"
    pluginName = "mainframer-integration"
    publish.username = project.findProperty("MF_PUBLISH_USER_NAME") as String?
    publish.password = project.findProperty("MF_PUBLISH_PASSWORD") as String?
    publish.setChannel(project.findProperty("publishChannel") as String?)
    updateSinceUntilBuild = false
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


val versionNumber by project
task("updateVersion") {
    doLast {
        val versionFile = File("version.gradle.properties")
        val matcher = Pattern.compile("version=(\\d+.\\d+.\\d+)").matcher(versionFile.readText())
        if (matcher.find()) {
            val versionText = StringBuilder(versionFile.readText()).replace(matcher.start(1), matcher.end(1), versionNumber.toString())
            versionFile.writeText(versionText.toString())
        }
    }
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile(kotlinModule("reflect"))
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