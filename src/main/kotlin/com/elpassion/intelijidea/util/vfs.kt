package com.elpassion.intelijidea.util

import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.hasChild(name: String) = findChild(name) != null