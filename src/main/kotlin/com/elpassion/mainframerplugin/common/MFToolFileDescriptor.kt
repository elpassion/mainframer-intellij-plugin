package com.elpassion.mainframerplugin.common

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory

val mfToolDescriptor: FileChooserDescriptor by lazy { FileChooserDescriptorFactory.createSingleFileDescriptor("sh") }