package com.elpassion.intelijidea.reporter

import com.intellij.errorreport.bean.ErrorBean
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.project.Project
import io.reactivex.functions.Consumer
import java.awt.Component

class MFErrorHandler : ErrorReportSubmitter() {
    override fun getReportActionText(): String {
        return "<html>Error Submitting Feedback: {0}<br>\\\n" +
                "Consider creating an issue at \\\n" +
                "<a href=\"https://github.com/elpassion/mainframer-intellij-plugin/issues\">Github Issue Tracker</a></html>"
    }

    override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: com.intellij.util.Consumer<SubmittedReportInfo>): Boolean {
        val crashData = collectCrashData(additionalInfo, events[0])
        val reportData = normalizeCrashData(crashData)
        val dataContext = DataManager.getInstance().getDataContext(parentComponent)
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        doSubmit(consumer.toReactive(), reportData, project)
        return true
    }

    private fun normalizeCrashData(bean: ErrorBean): Map<String, String> {
        return normalizeCrashData(bean, ApplicationInfoEx.getInstanceEx(), ApplicationNamesInfo.getInstance())
    }

    private fun doSubmit(consumer: Consumer<SubmittedReportInfo>, reportValues: Map<String, String>, project: Project?) {
        ReportService().uploadReport(project, reportValues)
                .doOnNext(consumer)
                .subscribe()
    }
}

private fun <T> com.intellij.util.Consumer<T>.toReactive(): Consumer<T> = Consumer {
    consume(it)
}
