package com.elpassion.intelijidea.reporter

import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.project.Project
import io.reactivex.Observable

class ReportService {
    fun uploadReport(project: Project?, reportValues: Map<String, String>)
            = Observable.error<SubmittedReportInfo>(NotImplementedError())
}