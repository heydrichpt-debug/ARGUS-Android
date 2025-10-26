package com.divaneural.argus.util

import android.content.Context
import com.divaneural.argus.R

object Prefs {
    private const val NAME = "argus_prefs"
    private const val KEY_API = "api_base"
    private const val KEY_USER = "admin_user"
    private const val KEY_PASS = "admin_pass"
    private const val KEY_BETA = "receive_betas"

    fun getApiBase(ctx: Context): String {
        val d = ctx.getString(R.string.default_api_base)
        return ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(KEY_API, d) ?: d
    }
    fun setApiBase(ctx: Context, v: String) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(KEY_API, v).apply()
    }
    fun getAdminUser(ctx: Context): String? =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(KEY_USER, null)
    fun setAdminUser(ctx: Context, v: String?) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(KEY_USER, v).apply()
    }
    fun getAdminPass(ctx: Context): String? =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(KEY_PASS, null)
    fun setAdminPass(ctx: Context, v: String?) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(KEY_PASS, v).apply()
    }
    fun getReceiveBetas(ctx: Context): Boolean =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(KEY_BETA, false)
    fun setReceiveBetas(ctx: Context, v: Boolean) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_BETA, v).apply()
    }
}
