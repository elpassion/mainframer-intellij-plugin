package com.elpassion.intelijidea.reporter

import com.elpassion.intelijidea.action.configure.releases.api.provideGitHubRetrofit

class ReportService {

    private val issueApi by lazy { provideGitHubRetrofit().create(GithubIssueApi::class.java) }

    fun uploadReport(reportValues: Map<String, String>)
            = issueApi.createIssue(convertToGithubIssueFormat(reportValues))
}