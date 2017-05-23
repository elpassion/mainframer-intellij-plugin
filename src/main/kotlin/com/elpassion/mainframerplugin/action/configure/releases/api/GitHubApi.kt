package com.elpassion.mainframerplugin.action.configure.releases.api

import io.reactivex.Single
import retrofit2.http.GET

interface GitHubApi {
    @GET("repos/gojuno/mainframer/releases")
    fun listReleases(): Single<List<ReleaseApiModel>>
}