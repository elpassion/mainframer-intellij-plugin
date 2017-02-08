package com.elpassion.intelijidea.util

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent


fun JTextComponent.textChanges(): Observable<String> {
    return JTextComponentTextObservable(this)
}

private class JTextComponentTextObservable(private val component: JTextComponent) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>) {
        val listener = Listener(component, observer)
        observer.onSubscribe(listener)
        component.document.addDocumentListener(listener)
        observer.onNext(component.text)
    }

    internal class Listener(private val component: JTextComponent, private val observer: Observer<in String>) : Disposable, DocumentListener {
        private var disposed = false
        override fun isDisposed() = disposed;

        override fun changedUpdate(e: DocumentEvent?) {
            notifyChanges()
        }

        override fun insertUpdate(e: DocumentEvent?) {
            notifyChanges()
        }

        override fun removeUpdate(e: DocumentEvent?) {
            notifyChanges()
        }

        private fun notifyChanges() {
            if (!isDisposed) {
                observer.onNext(component.text)
            }
        }

        override fun dispose() {
            component.document.removeDocumentListener(this)
            disposed = true
        }
    }
}
