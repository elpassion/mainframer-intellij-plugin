package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi

fun mfReleasesFetcher(api: GitHubApi) = {
    api.listReleases()
            .map { it.map { it.tagName.drop(1) } }
}