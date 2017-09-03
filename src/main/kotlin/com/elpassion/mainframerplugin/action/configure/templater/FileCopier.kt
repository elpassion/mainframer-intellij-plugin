package com.elpassion.mainframerplugin.action.configure.templater

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import io.reactivex.Completable
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

val resourceCopier: FileCopier = { source, target ->
    val classLoader = ClassLoaderHolder.javaClass.classLoader
    Completable.create { emitter ->
        ApplicationManager.getApplication().runWriteAction {
            try {
                val targetFile = File(target)
                FileUtil.createParentDirs(targetFile)
                Files.copy(classLoader.getResourceAsStream(source), Paths.get(targetFile.toURI()), StandardCopyOption.REPLACE_EXISTING)
                emitter.onComplete()
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }
    }
}

private object ClassLoaderHolder