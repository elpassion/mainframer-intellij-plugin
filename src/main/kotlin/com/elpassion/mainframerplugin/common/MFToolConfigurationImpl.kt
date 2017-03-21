package com.elpassion.mainframerplugin.common

import com.intellij.openapi.util.io.FileUtil
import java.io.*
import java.util.*

class MFToolConfigurationImpl(projectDir: String?) : MFToolConfiguration {

    private val configurationFile = File(File(projectDir, ".mainframer"), "config")

    override fun readRemoteMachineName(): String? {
        return configurationFile.asProperties().getProperty(REMOTE_BUILD_MACHINE_KEY)
    }

    override fun writeRemoteMachineName(name: String) {
        configurationFile.asProperties().run {
            setProperty(REMOTE_BUILD_MACHINE_KEY, name)
            saveToFile(configurationFile)
        }
    }

    companion object {
        private val REMOTE_BUILD_MACHINE_KEY = "remote_machine"
    }
}

interface MFToolConfiguration {
    fun readRemoteMachineName(): String?

    fun writeRemoteMachineName(name: String)
}

private fun File.asProperties(): Properties {
    if (isDirectory) {
        throw IllegalArgumentException(String.format("The path '%1\$s' belongs to a directory!", path))
    }
    if (!exists()) {
        return Properties()
    }
    return loadProperties()
}

private fun Properties.saveToFile(filePath: File) {
    FileUtil.createParentDirs(filePath)
    FileOutputStream(filePath).use { out ->
        store(out, null)
    }
}

private fun File.loadProperties(): Properties {
    val inputStreamReader = InputStreamReader(BufferedInputStream(FileInputStream(this)), Charsets.UTF_8)
    return Properties().apply { inputStreamReader.use { load(it) } }
}