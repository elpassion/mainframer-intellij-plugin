package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultOptionsConfigurable
import com.intellij.ide.ui.search.SearchableOptionContributor
import com.intellij.ide.ui.search.SearchableOptionProcessor

class MFSearchableOptionsContributor : SearchableOptionContributor() {
    override fun processOptions(processor: SearchableOptionProcessor) {
        processor.addOptions(
                "Mainframer",
                null,
                "Mainframer",
                MFBeforeTaskDefaultOptionsConfigurable.ID,
                MFBeforeTaskDefaultOptionsConfigurable.DISPLAY_NAME,
                true)
    }
}
