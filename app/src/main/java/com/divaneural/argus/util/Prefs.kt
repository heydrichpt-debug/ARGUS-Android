package com.divaneural.argus.util

import android.content.Context
import com.divaneural.argus.R

object Prefs {
    private const val PREFS = "argus_prefs"
    private const val KEY_API_BASE = "api_base"
    private const val KEY_RECEIVE_BETAS = "receive_betas"

    fun getApiBase(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_API_BASE, null)?.trim()
        return if (!saved.isNullOrEmpty()) saved else context.getString(R.string.default_api_base)
    }

    fun setApiBase(context: Context, value: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_API_BASE, value.trim()).apply()
    }

    fun getReceiveBetas(context: Context): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_RECEIVE_BETAS, false)
    }

    fun setReceiveBetas(context: Context, value: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_RECEIVE_BETAS, value).apply()
    }
}
