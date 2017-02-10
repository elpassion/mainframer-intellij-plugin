package com.elpassion.intelijidea.reporter

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GithubIssueApi {
    @POST("elpassion/mainframer-intellij-plugin/issues")
    fun createIssue(@Body issueBody: String): Observable<String>
}