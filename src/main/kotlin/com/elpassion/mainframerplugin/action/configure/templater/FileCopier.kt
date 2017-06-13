package com.elpassion.mainframerplugin.action.configure.templater

import com.intellij.openapi.application.ApplicationManager
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
                Files.copy(classLoader.getResourceAsStream(source), Paths.get(File(target).toURI()), StandardCopyOption.REPLACE_EXISTING)
                emitter.onComplete()
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }
    }
}

private object ClassLoaderHolder