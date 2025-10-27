package com.divaneural.argus

import android.content.Context

object Prefs {
    private const val PREFS = "argus_prefs"

    fun getApiBase(ctx: Context): String {
        val p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return p.getString("api_base", null) ?: AuthDefaultsKt.getDefaultApiBase()
    }

    fun getAdminUser(ctx: Context): String {
        val p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val v = p.getString("admin_user", null)
        return if (v.isNullOrBlank()) AuthDefaultsKt.getAdminUser() else v
    }

    fun getAdminPass(ctx: Context): String {
        val p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val v = p.getString("admin_pass", null)
        return if (v.isNullOrBlank()) AuthDefaultsKt.getAdminPass() else v
    }
}
