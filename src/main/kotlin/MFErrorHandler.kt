import com.intellij.openapi.diagnostic.ErrorReportSubmitter

class MFErrorHandler : ErrorReportSubmitter() {
    override fun getReportActionText(): String {
        return "<html>Error Submitting Feedback: {0}<br>\\\n" +
                "Consider creating an issue at \\\n" +
                "<a href=\"https://github.com/elpassion/mainframer-intellij-plugin/issues\">Github Issue Tracker</a></html>"
    }
}
