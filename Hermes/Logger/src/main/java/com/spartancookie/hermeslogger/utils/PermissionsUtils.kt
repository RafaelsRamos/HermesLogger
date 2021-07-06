package com.spartancookie.hermeslogger.utils

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager

private fun isGranted(code: Int) = code == PackageManager.PERMISSION_GRANTED

fun hasWriteStoragePermission(context: Context): Boolean {
    val requiredPermission = WRITE_EXTERNAL_STORAGE
    return isGranted(context.checkCallingOrSelfPermission(requiredPermission))
}

