package com.elpassion.intelijidea.task

import com.intellij.execution.BeforeRunTask

class MFBeforeRunTask(var mainframerPath:String,
                      var buildCommand: String,
                      var taskName: String) : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID)