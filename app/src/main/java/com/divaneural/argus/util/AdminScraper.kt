package com.divaneural.argus.util

import android.app.Activity
import android.content.*
import android.net.Uri
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

object AdminScraper {

    private val client = OkHttpClient()

    fun buildAdminBase(apiBase: String, downloadsPath: String): String {
        val base = apiBase.trimEnd('/')
        val path = if (downloadsPath.startsWith("/")) downloadsPath else "/$downloadsPath"
        val root = base.replace(Regex("/api/?$"), "")
        return (root + path).trimEnd('/') + "/"
    }

    fun parseDateToken(s: String): Date? {
        val lc = s.lowercase(Locale.getDefault())
        val now = Calendar.getInstance()
        if (lc.contains("ontem")) { now.add(Calendar.DAY_OF_MONTH, -1); return now.time }
        if (lc.contains("hoje")) { return now.time }
        Regex("(\\d{2})/(\\d{2})/(\\d{4})").find(lc)?.let {
            val (dd, mm, yyyy) = it.destructured
            return GregorianCalendar(yyyy.toInt(), mm.toInt()-1, dd.toInt()).time
        }
        Regex("(\\d{4})-(\\d{2})-(\\d{2})").find(lc)?.let {
            val (yyyy, mm, dd) = it.destructured
            return GregorianCalendar(yyyy.toInt(), mm.toInt()-1, dd.toInt()).time
        }
        return null
    }

    fun scrapeLinks(adminBase: String, user: String?, pass: String?): List<String> {
        val reqBuilder = Request.Builder().url(adminBase)
        if (!user.isNullOrBlank() && !pass.isNullOrBlank()) {
            reqBuilder.header("Authorization", Credentials.basic(user, pass))
        }
        val resp = client.newCall(reqBuilder.build()).execute()
        if (!resp.isSuccessful) return emptyList()
        val body = resp.body?.string() ?: return emptyList()
        val doc = Jsoup.parse(body, adminBase)
        return doc.select("a[href]").map { it.absUrl("href") }
            .filter { it.lowercase().matches(Regex(".*\\.(apk|zip|pdf|jpg|jpeg|png)$")) }
            .distinct()
    }

    fun pickCandidates(links: List<String>, wantsBackup: Boolean, wantsType: String?, date: Date?, latest: Boolean): List<String> {
        fun score(u: String): Int {
            var s = 0; val lu = u.lowercase(Locale.getDefault())
            if (wantsBackup && lu.contains("backup")) s += 3
            if (!wantsType.isNullOrBlank() && lu.endsWith(wantsType)) s += 2
            if (date != null) {
                val yyyy = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                val mm = SimpleDateFormat("MM", Locale.getDefault()).format(date)
                val dd = SimpleDateFormat("dd", Locale.getDefault()).format(date)
                if (lu.contains("${yyyy}${mm}${dd}") || lu.contains("${yyyy}-${mm}-${dd}") || lu.contains("${dd}${mm}${yyyy}")) s += 3
            }
            if (latest) s += 1
            return s
        }
        return links.map { it to score(it) }.sortedByDescending { it.second }.map { it.first }
    }

    fun guessBackup(adminBase: String, date: Date?): String {
        val d = date ?: Date()
        val yyyy = SimpleDateFormat("yyyy", Locale.getDefault()).format(d)
        val mm = SimpleDateFormat("MM", Locale.getDefault()).format(d)
        val dd = SimpleDateFormat("dd", Locale.getDefault()).format(d)
        return adminBase + "DIVA_BACKUP_${yyyy}${mm}${dd}.zip"
    }

    fun guessMime(name: String): String {
        val n = name.lowercase(Locale.getDefault())
        return when {
            n.endsWith(".apk") -> "application/vnd.android.package-archive"
            n.endsWith(".zip") -> "application/zip"
            n.endsWith(".pdf") -> "application/pdf"
            n.endsWith(".jpg") || n.endsWith(".jpeg") -> "image/jpeg"
            n.endsWith(".png") -> "image/png"
            else -> "application/octet-stream"
        }
    }

    private const val REQ_CREATE_DOC = 1001
    private var pending: Attachment? = null

    fun downloadWithCreateDocument(activity: Activity, att: Attachment) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = att.mime ?: "application/octet-stream"
            putExtra(Intent.EXTRA_TITLE, att.name ?: "arquivo")
        }
        pending = att
        activity.startActivityForResult(intent, REQ_CREATE_DOC)
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CREATE_DOC && resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data ?: return
            val att = pending ?: return
            pending = null
            Thread {
                try {
                    val req = Request.Builder().url(att.url).build()
                    client.newCall(req).execute().use { resp ->
                        val body = resp.body ?: return@use
                        activity.contentResolver.openOutputStream(uri)?.use { out ->
                            body.byteStream().use { input -> input.copyTo(out) }
                        }
                    }
                } catch (_: Exception) {}
            }.start()
        }
    }

    fun copyToClipboard(ctx: Context, text: String) {
        val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("url", text))
    }
}
