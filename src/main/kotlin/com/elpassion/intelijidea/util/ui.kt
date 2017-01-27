package com.elpassion.intelijidea.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint

fun showBalloon(project: Project?, message: String) {
    val statusBar = WindowManager.getInstance().getStatusBar(project)
    JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
            .setFadeoutTime(5000)
            .createBalloon()
            .show(RelativePoint.getNorthEastOf(statusBar.component), Balloon.Position.above)
}