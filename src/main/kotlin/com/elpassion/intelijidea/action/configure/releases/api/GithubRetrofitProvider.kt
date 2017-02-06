package com.elpassion.intelijidea.action.configure.releases.api

import com.elpassion.intelijidea.util.objectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

fun provideGithubApi(retrofit: Retrofit): GitHubApi = retrofit
        .create(GitHubApi::class.java)

fun provideGithubRetrofit(): Retrofit = provideBaseRetrofitBuilder()
        .baseUrl("https://api.github.com")
        .build()

fun provideBaseRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
