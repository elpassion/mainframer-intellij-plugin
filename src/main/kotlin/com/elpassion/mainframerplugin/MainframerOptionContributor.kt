package com.elpassion.mainframerplugin

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.task.TaskDefaultOptionsConfigurable
import com.intellij.ide.ui.search.SearchableOptionContributor
import com.intellij.ide.ui.search.SearchableOptionProcessor

class MainframerOptionContributor : SearchableOptionContributor() {
    override fun processOptions(processor: SearchableOptionProcessor) {
        processor.addOptions(
                StringsBundle.getMessage("searchable.options.mainframer.text"),
                null,
                StringsBundle.getMessage("searchable.options.mainframer.hint"),
                TaskDefaultOptionsConfigurable.ID,
                TaskDefaultOptionsConfigurable.DISPLAY_NAME,
                true)
    }
}
