package com.elpassion.intelijidea.common

import com.intellij.openapi.util.io.FileUtil
import java.io.*
import java.util.*

class LocalProperties(projectDir: String?) {

    private val localPropertiesFile = File(projectDir, "local.properties")

    fun readRemoteMachineName(): String? {
        return localPropertiesFile.asProperties().getProperty(REMOTE_BUILD_MACHINE_KEY)
    }

    fun writeRemoteMachineName(name: String) {
        val properties = localPropertiesFile.asProperties()
        properties.setProperty(REMOTE_BUILD_MACHINE_KEY, name)
        properties.saveToFile(localPropertiesFile)
    }

    companion object {
        private val REMOTE_BUILD_MACHINE_KEY = "remote_build.machine"
    }
}

private fun File.asProperties(): Properties {
    if (isDirectory) {
        throw IllegalArgumentException(String.format("The path '%1\$s' belongs to a directory!", path))
    }
    if (!exists()) {
        return Properties()
    }
    val properties = Properties()
    InputStreamReader(BufferedInputStream(FileInputStream(this)), Charsets.UTF_8).use { reader -> properties.load(reader) }
    return properties
}

private fun Properties.saveToFile(filePath: File) {
    FileUtil.createParentDirs(filePath)
    FileOutputStream(filePath).use { out ->
        store(out, null)
    }
}