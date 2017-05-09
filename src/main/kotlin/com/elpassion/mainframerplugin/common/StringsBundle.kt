package com.elpassion.mainframerplugin.common

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey

const val BUNDLE = "messages.Mainframer"

object StringsBundle : AbstractBundle(BUNDLE) {
    override fun getMessage(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String
            = super.getMessage(key, *params)
}