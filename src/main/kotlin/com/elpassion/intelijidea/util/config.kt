package com.elpassion.intelijidea.util

val mfVersion = "2.0.0"
val mfFilename = "mainframer.sh"

fun getLatestMfToolDownloadUrl() = getMfToolDownloadUrl(mfVersion)

fun getMfToolDownloadUrl(version: String) = "https://github.com/gojuno/mainframer/releases/download/v$version/$mfFilename"