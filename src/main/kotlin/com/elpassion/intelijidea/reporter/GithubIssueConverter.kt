package com.elpassion.intelijidea.reporter

import com.elpassion.intelijidea.util.toJson

fun convertToGithubIssueFormat(environmentDetails: Map<String, String>): String {
    return mapOf(
            "title" to "[auto-generated] Crash in plugin",
            "body" to generateGithubIssueBody(environmentDetails)
    ).toJson()
}

private fun generateGithubIssueBody(body: Map<String, String>): String {
    val errorMessage = body["error.message"] ?: "invalid error"
    val stackTrace = body["error.stacktrace"] ?: "invalid stacktrace"
    val errorDescription = body["error.description"]?.let { it + "\n\n" } ?: ""
    val bodyAsString = bodyToString(stripBody(body))
    return errorDescription +
            bodyAsString +
            "\n```\n$stackTrace\n```\n" +
            "\n```\n$errorMessage\n```"
}

private fun bodyToString(body: Map<String, String>) = body
        .map { e -> e.key + ": " + e.value + "\n" }
        .joinToString()

private fun stripBody(body: Map<String, String>) = body
        .filter { e ->
            e.key != "error.description" && e.key != "error.message" && e.key != "error.stacktrace"
        }