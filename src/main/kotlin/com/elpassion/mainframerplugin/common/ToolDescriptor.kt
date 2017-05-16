package com.elpassion.mainframerplugin.common

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory

val toolDescriptor: FileChooserDescriptor by lazy { FileChooserDescriptorFactory.createSingleFileDescriptor("sh") }