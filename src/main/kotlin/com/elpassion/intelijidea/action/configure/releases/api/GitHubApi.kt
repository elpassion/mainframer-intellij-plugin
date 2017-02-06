package com.elpassion.intelijidea.action.configure.releases.api

import io.reactivex.Observable
import retrofit2.http.GET

interface GitHubApi {
    @GET("repos/gojuno/mainframer/releases")
    fun listReleases(): Observable<List<ReleaseApiModel>>
}