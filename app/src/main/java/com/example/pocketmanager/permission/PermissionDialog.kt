package com.example.pocketmanager.permission

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.pocketmanager.R
import com.google.android.material.snackbar.Snackbar

internal class PermissionDialog(private val activity: Activity) {
    fun show() {
        val view =
            activity.layoutInflater.inflate(R.layout.info_permission, null)
        AlertDialog.Builder(activity)
            .setView(view)
            .setTitle(R.string.app_name)
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton(android.R.string.ok, OkClick(activity))
            .setNegativeButton(android.R.string.cancel, CancelClick(activity))
            .create()
            .show()
    }

    internal class OkClick(private val activity: Activity) : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            requestPermissionsAndroidM()
            dialog.dismiss()
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun requestPermissionsAndroidM() {
            if (BuildUtils.isMinVersionM) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.RECEIVE_SMS
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.READ_SMS
                    )
                ) {
                    Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        "Please Grant Permissions to get Transaction SMS Responses.",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "ENABLE"
                    ) {
                        activity.requestPermissions(
                            ApplicationPermission.PERMISSIONS,
                            ApplicationPermission.REQUEST_CODE
                        )
                    }.show()
                } else {
                    activity.requestPermissions(
                        ApplicationPermission.PERMISSIONS,
                        ApplicationPermission.REQUEST_CODE
                    )
                }
            }
        }

    }

    internal class CancelClick(private val activity: Activity) : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            dialog.dismiss()
            activity.finish()
        }

    }

}