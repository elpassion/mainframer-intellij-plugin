package com.elpassion.intelijidea.action.configure.configurator

data class MFConfiguratorOut(val version: String,
                             val remoteName: String,
                             val taskName: String,
                             val buildCommand: String)