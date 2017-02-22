package com.elpassion.intelijidea.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import javax.swing.SwingUtilities
import javax.swing.event.HyperlinkEvent

fun showInfo(project: Project, message: String, hyperlinkListener: ((HyperlinkEvent) -> Unit)? = null)  {
    showBalloon(project, message, MessageType.INFO, hyperlinkListener)
}

fun showError(project: Project, message: String, hyperlinkListener: ((HyperlinkEvent) -> Unit)? = null) {
    showBalloon(project, message, MessageType.ERROR, hyperlinkListener)
}

private fun showBalloon(project: Project, message: String, messageType: MessageType,
                        hyperlinkListener: ((HyperlinkEvent) -> Unit)?) = SwingUtilities.invokeAndWait {
    val statusBar = WindowManager.getInstance().getStatusBar(project)
    JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(message, messageType, hyperlinkListener)
            .setFadeoutTime(5000)
            .createBalloon()
            .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.above)
}