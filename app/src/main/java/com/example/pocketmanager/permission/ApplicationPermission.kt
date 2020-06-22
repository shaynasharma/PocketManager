package com.example.pocketmanager.permission

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

internal class ApplicationPermission @JvmOverloads constructor(
    private val activity: Activity,
    private val permissionDialog: PermissionDialog = PermissionDialog(
        activity
    )
) {
    fun check() {
        if (isGranted) {
            return
        }
        if (activity.isFinishing) {
            return
        }
        permissionDialog.show()
    }

    fun isGranted(requestCode: Int, grantResults: IntArray): Boolean {
        return requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    val isGranted: Boolean
        get() = !BuildUtils.isMinVersionM || isGrantedAndroidM

    @get:TargetApi(Build.VERSION_CODES.M)
    private val isGrantedAndroidM: Boolean
        get() = activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                activity.checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED

    companion object {
        @JvmField
        val PERMISSIONS = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )
        const val REQUEST_CODE = 0x123450
    }

}