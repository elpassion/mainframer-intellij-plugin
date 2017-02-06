package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import io.reactivex.Observable


class MFVersionsReleaseService(private val api: GitHubApi) {
    fun getVersions(): Observable<List<String>> = api.listReleases()
            .map { it.map { it.tagName.drop(1) } }
}