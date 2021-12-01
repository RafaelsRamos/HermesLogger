package com.spartancookie.hermeslogger.utils

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import com.spartancookie.hermeslogger.share.sdk29AndUp

private fun isGranted(code: Int) = code == PackageManager.PERMISSION_GRANTED

/**
 * Check if the user granted [WRITE_EXTERNAL_STORAGE] permission
 */
fun canShareLogDumps(context: Context): Boolean {
    val requiredPermission = WRITE_EXTERNAL_STORAGE
    val isUsingAndroidXActivity = context as? ComponentActivity != null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isUsingAndroidXActivity
    } else {
        isGranted(context.checkCallingOrSelfPermission(requiredPermission))
    }
}