package com.elpassion.intelijidea.common.console

class RemoteToLocalInputConverter(projectName: String) {
    val PATH_SEGMENT = "(?:/\\w+(?:\\.\\w+)*)*?"
    private val FILE_EXTENSION = "\\.\\w+"
    private val END_PATH = "($PATH_SEGMENT$FILE_EXTENSION)*"
    private val REMOTE_START_PATH = "(?:$PATH_SEGMENT)*"
    private val REMOTE_PATH = "$REMOTE_START_PATH/mainframer/$projectName"
    val FILE_PATH_REGEX = "(?:$REMOTE_PATH$END_PATH)"
    private val LINE_NUMBER_START = ":(?:\\s\\()?"
    private val LINE_NUMBER_VALUE = "(\\d+)"
    private val LINE_NUMBER_END = "(?:,\\s\\d+\\))?"
    val LINE_NUMBER_REGEX = "$LINE_NUMBER_START$LINE_NUMBER_VALUE$LINE_NUMBER_END"
    val FIRST_FRAGMENT_REGEX = "(?:.*?:\\s)?"
    val LINE_WITH_REMOTE_EXCEPTION = "$FIRST_FRAGMENT_REGEX$FILE_PATH_REGEX$LINE_NUMBER_REGEX".toRegex()
}