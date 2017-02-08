package com.elpassion.intelijidea.task.edit;

import io.reactivex.Observable;

public interface TaskEditView {

    void enableAcceptButton();

    void disableAcceptButton();

    interface Actions {
        Observable<String> observeTaskName();
    }

}
