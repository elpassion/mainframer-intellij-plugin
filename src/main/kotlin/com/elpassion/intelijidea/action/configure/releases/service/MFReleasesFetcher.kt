package com.elpassion.intelijidea.action.configure.releases.service

import com.elpassion.intelijidea.action.configure.releases.api.GitHubApi
import io.reactivex.Observable

class MFReleasesFetcher(private val api: GitHubApi) : Function0<Observable<List<String>>> {

    override fun invoke(): Observable<List<String>> = api.listReleases()
            .map { it.map { it.tagName.drop(1) } }
}