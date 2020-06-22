package com.example.pocketmanager.permission

import android.os.Build

class BuildUtils private constructor() {
    companion object {
        val isMinVersionQ: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        val isMinVersionP: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

        val isVersionP: Boolean
            get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.P

        val isMinVersionN: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

        val isMinVersionM: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        val isMinVersionL: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    init {
        throw IllegalStateException("Utility class")
    }
}