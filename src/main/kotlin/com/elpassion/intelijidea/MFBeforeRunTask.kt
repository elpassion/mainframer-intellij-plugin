package com.elpassion.intelijidea

import com.intellij.execution.BeforeRunTask

class MFBeforeRunTask(var command: String) : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID)