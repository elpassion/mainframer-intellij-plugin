package com.elpassion.intelijidea.reporter

import com.intellij.errorreport.bean.ErrorBean
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.util.SystemProperties

fun normalizeCrashData(error: ErrorBean,
                       appInfo: ApplicationInfoEx,
                       namesInfo: ApplicationNamesInfo): Map<String, String> {
    val params = mapOf("error.description" to error.description,
            "Plugin Name" to error.pluginName,
            "Plugin Version" to error.pluginVersion,
            "OS Name" to SystemProperties.getOsName(),
            "Java version" to SystemProperties.getJavaVersion(),
            "Java vm vendor" to SystemProperties.getJavaVmVendor(),
            "App Name" to namesInfo.productName,
            "App Full Name" to namesInfo.fullProductName,
            "App Version name" to appInfo.versionName,
            "Is EAP" to appInfo.isEAP.toString(),
            "App Build" to appInfo.build.asString(),
            "App Version" to appInfo.fullVersion,
            "Last Action" to error.lastAction,
            "error.message" to error.message,
            "error.stacktrace" to error.stackTrace)

    val attachments = error.attachments.mapIndexed { i, attachment ->
        listOf("attachment.name.$i" to attachment.name,
                "attachment.value.$i" to attachment.encodedBytes)
    }.flatten()
    return params + attachments
}