package com.spartancookie.hermeslogger.utils

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity

private fun isGranted(code: Int) = code == PackageManager.PERMISSION_GRANTED

/**
 * Check if the user has the necessary permissions/conditions to share hermes log dump.
 *
 * Due to [scoped storage][https://developer.android.com/about/versions/11/privacy/storage] introduced in Android 10+,
 * In API versions lower than 29, the user is required to have granted [WRITE_EXTERNAL_STORAGE] permission.
 * In API versions higher than 29, the user does not require permissions at all. However, when sharing the log dump,
 * the user will have to select a place to store the log dump file on.
 */
fun canShareHermesLogDumps(context: Context): Boolean {

    require(context as? Activity != null) {
        "context provided must be fetched from an Activity, and not from the Application."
    }

    val requiredPermission = WRITE_EXTERNAL_STORAGE
    val isUsingAndroidXActivity = context as? ComponentActivity != null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isUsingAndroidXActivity
    } else {
        isGranted(context.checkCallingOrSelfPermission(requiredPermission))
    }
}