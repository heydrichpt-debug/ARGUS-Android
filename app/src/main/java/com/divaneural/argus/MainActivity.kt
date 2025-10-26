package com.divaneural.argus

import android.content.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.divaneural.argus.ui.ConfigActivity
import com.divaneural.argus.ui.MenuActivity
import com.divaneural.argus.util.AdminScraper
import com.divaneural.argus.util.Attachment
import com.divaneural.argus.util.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var scroll: ScrollView
    private lateinit var chat: LinearLayout
    private lateinit var input: EditText
    private lateinit var btnPlus: Button
    private lateinit var btnMic: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        scroll = findViewById(R.id.scroll)
        chat = findViewById(R.id.chatContainer)
        input = findViewById(R.id.input)
        btnPlus = findViewById(R.id.btnPlus)
        btnMic = findViewById(R.id.btnMic)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "ARGUS"
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        pushBot("Como posso ajudar?")

        btnPlus.setOnClickListener { /* visual apenas */ }
        btnMic.setOnClickListener {
            pushBot("[Mic] Gravando (stub). Fale e toque no mic novamente para parar. O texto não será enviado automaticamente.")
        }

        input.setOnEditorActionListener { v, actionId, event ->
            val t = input.text.toString().trim()
            if (t.isNotEmpty()) {
                pushMe(t); input.setText("")
                handleQuery(t)
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AdminScraper.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Config").setIcon(android.R.drawable.ic_menu_preferences).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, ConfigActivity::class.java))
        return true
    }

    private fun pushMe(text: String) {
        val tv = layoutInflater.inflate(R.layout.view_message_me, chat, false) as TextView
        tv.text = text
        chat.addView(tv); scrollBottom()
    }
    private fun pushBot(text: String) {
        val tv = layoutInflater.inflate(R.layout.view_message_bot, chat, false) as TextView
        tv.text = text
        chat.addView(tv); scrollBottom()
    }
    private fun pushAttachment(att: Attachment) {
        val v = layoutInflater.inflate(R.layout.view_attachment, chat, false)
        v.findViewById<TextView>(R.id.title).text = att.name ?: att.url
        v.findViewById<TextView>(R.id.meta).text = att.mime ?: "arquivo"
        v.findViewById<Button>(R.id.btnDownload).setOnClickListener { AdminScraper.downloadWithCreateDocument(this, att) }
        v.findViewById<Button>(R.id.btnCopy).setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("url", att.url))
        }
        chat.addView(v); scrollBottom()
    }
    private fun scrollBottom() { scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) } }

    private fun handleQuery(q: String) {
        val wantsBackup = Regex("backup|c[oó]pia").containsMatchIn(q.lowercase())
        val typeMatch = Regex("\b(pdf|zip|apk|imagem|jpg|jpeg|png)\b").find(q.lowercase())
        val wantsType = typeMatch?.groupValues?.get(1)
        val latest = Regex("(últim|mais recente)", RegexOption.IGNORE_CASE).containsMatchIn(q)
        val date = AdminScraper.parseDateToken(q)

        val base = Prefs.getApiBase(this)
        val adminBase = AdminScraper.buildAdminBase(base, "/admin")

        Thread {
            val links = AdminScraper.scrapeLinks(adminBase, Prefs.getAdminUser(this), Prefs.getAdminPass(this))
            val candidates = AdminScraper.pickCandidates(links, wantsBackup, wantsType, date, latest)

            runOnUiThread {
                if (candidates.isEmpty() && wantsBackup) {
                    val guess = AdminScraper.guessBackup(adminBase, date)
                    pushBot("Não encontrei. Sugestão:")
                    pushAttachment(Attachment(guess, guess.substringAfterLast('/'), "application/zip"))
                } else if (candidates.isEmpty()) {
                    pushBot("Não encontrei nada. Tente especificar uma data ou tipo (pdf/zip/apk/imagem).")
                } else {
                    pushBot("Encontrei isto:")
                    candidates.take(5).forEach { url ->
                        val name = url.substringAfterLast('/')
                        val mime = AdminScraper.guessMime(name)
                        pushAttachment(Attachment(url, name, mime))
                    }
                }
            }
        }.start()
    }
}
