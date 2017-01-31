package com.elpassion.intelijidea.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import javax.swing.event.HyperlinkEvent

fun showError(project: Project, message: String, hyperlinkListener: ((HyperlinkEvent) -> Unit)? = null) {
    val statusBar = WindowManager.getInstance().getStatusBar(project)
    JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(message, MessageType.ERROR, hyperlinkListener)
            .setFadeoutTime(5000)
            .createBalloon()
            .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.above)
}

fun showBalloon(project: Project?, message: String) {
    val statusBar = WindowManager.getInstance().getStatusBar(project)
    JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
            .setFadeoutTime(5000)
            .createBalloon()
            .show(RelativePoint.getNorthEastOf(statusBar.component), Balloon.Position.above)
}