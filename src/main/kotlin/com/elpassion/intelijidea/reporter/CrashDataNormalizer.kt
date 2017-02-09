package com.elpassion.intelijidea.reporter

import com.intellij.errorreport.bean.ErrorBean
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.util.SystemProperties

fun normalizeCrashData(error: ErrorBean,
                       appInfo: ApplicationInfoEx,
                       namesInfo: ApplicationNamesInfo): Map<String, String> {
    return errorInfo(error) +
            pluginInfo(error) +
            ideaInfo(namesInfo, appInfo) +
            systemInfo() +
            errorAttachments(error)
}

private fun errorInfo(error: ErrorBean): Map<String, String> {
    return mapOf(
            "error.description" to error.description,
            "Last Action" to error.lastAction,
            "error.message" to error.message,
            "error.stacktrace" to error.stackTrace)
}

private fun pluginInfo(error: ErrorBean) = mapOf(
        "Plugin Name" to error.pluginName,
        "Plugin Version" to error.pluginVersion)

private fun ideaInfo(namesInfo: ApplicationNamesInfo,
                     appInfo: ApplicationInfoEx) = mapOf(
        "App Name" to namesInfo.productName,
        "App Full Name" to namesInfo.fullProductName,
        "App Version name" to appInfo.versionName,
        "Is EAP" to appInfo.isEAP.toString(),
        "App Build" to appInfo.build.asString(),
        "App Version" to appInfo.fullVersion)

private fun systemInfo() = mapOf("OS Name" to SystemProperties.getOsName(),
        "Java version" to SystemProperties.getJavaVersion(),
        "Java vm vendor" to SystemProperties.getJavaVmVendor()
)

private fun errorAttachments(error: ErrorBean): List<Pair<String, String>> {
    return error.attachments.mapIndexed { i, attachment ->
        listOf("attachment.name.$i" to attachment.name,
                "attachment.value.$i" to attachment.encodedBytes)
    }.flatten()
}