package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import io.reactivex.Single

fun mfReleasesFetcher(api: GitHubApi) = {
    api.listReleases()
            .mapWithoutVPrefix()
            .drop1xVersions()
}

private fun Single<List<String>>.drop1xVersions() = map { it.filter { !it.matches("1.\\d.\\d".toRegex()) } }

private fun Single<List<ReleaseApiModel>>.mapWithoutVPrefix() = map { it.map { it.tagName.drop(1) } }
