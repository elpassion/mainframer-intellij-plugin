package com.elpassion.intelijidea.tools

import com.elpassion.intelijidea.util.textChanges
import org.junit.Test
import javax.swing.JTextField

class JTextFieldExtensionTest {
    val jTextField = JTextField()

    @Test
    fun shouldStartWithEmptyStringWhenEditTextIsEmpty() {
        jTextField.textChanges()
                .test()
                .assertValues("")
    }

    @Test
    fun shouldStartWithInitialSentenceWhenEditTextIsNotEmpty() {
        JTextField("initial text")
                .textChanges()
                .test()
                .assertValues("initial text")
    }

    @Test
    fun shouldEmitChangedText() {
        val testObserver = jTextField.textChanges()
                .test()

        jTextField.text = "changed text"

        testObserver.assertValues("", "changed text")
    }
}