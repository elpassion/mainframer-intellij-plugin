package com.elpassion.mainframerplugin.util

import java.util.regex.Pattern

val toolFilename = "mainframer"

fun getToolDownloadUrl(version: String): String {
    val filename = toolFilename + if (version.isVersionPre3()) ".sh" else ""
    return "https://github.com/gojuno/mainframer/releases/download/v$version/$filename"
}

private fun String.isVersionPre3(): Boolean {
    val matcher = Pattern.compile("^(\\d+)").matcher(this)
    return if (matcher.find()) {
        val version = matcher.group(1).toInt()
        version < 3
    } else false
}
