package com.elpassion.intelijidea.reporter

import com.intellij.diagnostic.IdeErrorsDialog
import com.intellij.diagnostic.LogMessageEx
import com.intellij.errorreport.bean.ErrorBean
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.idea.IdeaLogger
import com.intellij.openapi.diagnostic.Attachment
import com.intellij.openapi.diagnostic.IdeaLoggingEvent

fun collectCrashData(additionalInfo: String?, event: IdeaLoggingEvent): ErrorBean {
    val pluginDescriptor = findPluginWhichThrown(event.throwable)
    return ErrorBean(event.throwable, IdeaLogger.ourLastActionId).apply {
        description = additionalInfo
        message = event.message
        pluginName = pluginDescriptor?.name
        pluginVersion = pluginDescriptor?.version
        attachments = extractAttachments(event.data)
    }
}

private fun extractAttachments(data: Any?): MutableList<Attachment>? {
    return if (data is LogMessageEx) data.includedAttachments else null
}

private fun findPluginWhichThrown(throwable: Throwable): IdeaPluginDescriptor? {
    return IdeErrorsDialog.findPluginId(throwable)?.let { pluginId ->
        PluginManager.getPlugin(pluginId)?.let { pluginDescriptor ->
            if (pluginDescriptor.isBundled) {
                pluginDescriptor
            } else {
                null
            }
        }
    }
}