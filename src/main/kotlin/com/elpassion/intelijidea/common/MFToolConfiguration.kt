package com.elpassion.intelijidea.common

import com.intellij.openapi.util.io.FileUtil
import java.io.*
import java.util.*

class MFToolConfiguration(projectDir: String?) {

    private val configurationFile = File(File(projectDir, ".mainframer"), "config")

    fun readRemoteMachineName(): String? {
        return configurationFile.asProperties().getProperty(REMOTE_BUILD_MACHINE_KEY)
    }

    fun writeRemoteMachineName(name: String) {
        configurationFile.asProperties().run {
            setProperty(REMOTE_BUILD_MACHINE_KEY, name)
            saveToFile(configurationFile)
        }
    }

    companion object {
        private val REMOTE_BUILD_MACHINE_KEY = "remote_machine"
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