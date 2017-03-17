package com.elpassion.intelijidea.common.console

import com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteToLocalTranslatingInputFilterTest {

    private val project = mock<Project> {
        on { basePath } doReturn "/local/path/to/$PROJECT_NAME"
        on { name } doReturn PROJECT_NAME
    }
    private val filter = RemoteToLocalTranslatingInputFilter(project)

    @Test
    fun `should replace remote path with local`() {
        val result = filter.applyFilter("E: /remote/user/mainframer/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", ERROR_OUTPUT).first().first
        assertEquals("/local/path/to/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", result)
    }

    @Test
    fun `should really replace remote path with local`() {
        val result = filter.applyFilter("E: /remote/user/mainframer/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", ERROR_OUTPUT).first().first
        assertEquals("/local/path/to/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", result)
    }

    @Test
    fun `should also replace remote path with local during compilation errors`() {
        val result = filter.applyFilter("e: /remote/user/mainframer/$PROJECT_NAME/src/ExampleUnitTest.java: (15, 10): $errorMessage", ERROR_OUTPUT).first().first
        assertEquals("/local/path/to/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", result)
    }

    @Test
    fun `should replace remote path with local event if error is missing at the begging of line`() {
        val result = filter.applyFilter("/remote/user/mainframer/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", ERROR_OUTPUT).first().first
        assertEquals("/local/path/to/$PROJECT_NAME/src/ExampleUnitTest.java:15: $errorMessage", result)
    }

    companion object {
        private val PROJECT_NAME = "testProject"
        private val errorMessage = "error: cannot find pizza"
    }
}