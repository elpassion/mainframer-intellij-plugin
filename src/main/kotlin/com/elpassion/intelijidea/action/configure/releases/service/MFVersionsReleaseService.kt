package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi


class MFVersionsReleaseService(private val api: GitHubApi) {
    fun getVersions(): List<String> = api.listReleases()
            .execute().body()
            .map { it.tagName.drop(1) }
}