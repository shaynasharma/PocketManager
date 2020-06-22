package com.example.pocketmanager.permission

import android.app.Activity

class PermissionService(activity: Activity) {
    private val applicationPermission: ApplicationPermission
    fun check() {
        applicationPermission.check()
    }

    fun isGranted(requestCode: Int, grantResults: IntArray?): Boolean {
        return applicationPermission.isGranted(requestCode, grantResults!!)
    }

    val isPermissionGranted: Boolean
        get() = applicationPermission.isGranted

    init {
        applicationPermission = ApplicationPermission(activity)
    }
}