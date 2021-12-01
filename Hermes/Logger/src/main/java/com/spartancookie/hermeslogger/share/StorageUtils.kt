package com.spartancookie.hermeslogger.share

import android.os.Build

/**
 * Run func if device is Android 10+, returns null otherwise
 */
internal inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}