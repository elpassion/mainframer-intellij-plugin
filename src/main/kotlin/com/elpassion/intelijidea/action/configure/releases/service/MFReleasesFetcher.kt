package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.ReleaseApiModel
import io.reactivex.Single

fun mfReleasesFetcher(api: GitHubApi) = {
    api.listReleases()
            .mapWithoutVPrefix()
            .drop1xVersions()
}

private fun Single<List<String>>.drop1xVersions(): Single<List<String>>? {
    val versionOneRegex = "1.\\d.\\d".toRegex()
    return map { it.filter { !it.matches(versionOneRegex) } }
}

private fun Single<List<ReleaseApiModel>>.mapWithoutVPrefix() = map { it.map { it.tagName.drop(1) } }
