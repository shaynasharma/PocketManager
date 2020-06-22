package com.example.pocketmanager.permission

import android.app.Activity

object PermissionManager {
    fun checkPermission(activity: Activity?): Boolean {
        val permissionService = PermissionService(activity!!)
        if (!permissionService.isPermissionGranted) {
            permissionService.check()
        } else {
            return true
        }
        return permissionService.isPermissionGranted
    }
}