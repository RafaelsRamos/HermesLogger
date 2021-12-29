package com.rafaelsramos.hermes.utils

import android.content.Context

private const val HERMES_KEY = "hermes_prefs_key"

private fun getSharedPreferences(ctx: Context) = ctx.getSharedPreferences("application", Context.MODE_PRIVATE)

fun saveHermesPreference(context: Context, isEnabled: Boolean) {
    getSharedPreferences(context)
        .edit()
        .putBoolean(HERMES_KEY, isEnabled)
        .apply()
}

fun loadHermesPreference(context: Context): Boolean {
    return getSharedPreferences(context).getBoolean(HERMES_KEY, false)
}