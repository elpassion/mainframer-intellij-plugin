package com.elpassion.mainframerplugin.util

import java.util.regex.Pattern

val toolFilename = "mainframer"

fun getToolDownloadUrl(version: String): String {
    val filename = toolFilename + if (version.isPre3) ".sh" else ""
    return "https://github.com/gojuno/mainframer/releases/download/v$version/$filename"
}

private val String.isPre3: Boolean
    get() = majorVersion < 3

private val String.majorVersion: Int
    get() {
        val matcher = Pattern.compile("^(\\d+)").matcher(this)
        return if (matcher.find()) {
            matcher.group(1).toInt()
        } else throw RuntimeException("Invalid version format!")
    }