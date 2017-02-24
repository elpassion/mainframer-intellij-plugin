package com.elpassion.intelijidea.action.configure.configurator

data class MFConfiguratorIn(val versionList: List<String>,
                            val remoteName: String?,
                            val taskName: String?,
                            val buildCommand: String?)