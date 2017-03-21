package com.elpassion.mainframerplugin.action.configure.releases.api

import com.elpassion.mainframerplugin.util.objectMapper
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

fun provideGitHubApi(retrofit: Retrofit): GitHubApi = retrofit
        .create(GitHubApi::class.java)

fun provideGitHubRetrofit(): Retrofit = provideBaseRetrofitBuilder()
        .baseUrl("https://api.github.com")
        .build()

fun provideBaseRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
