package com.elpassion.intelijidea.action.configure.releases.api

import retrofit2.Call
import retrofit2.http.GET

interface GitHubApi {
    @GET("repos/gojuno/mainframer/releases")
    fun listReleases(): Call<List<ReleaseApiModel>>
}