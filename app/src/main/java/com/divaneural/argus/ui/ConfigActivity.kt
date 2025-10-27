package com.divaneural.argus.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {

    private val prefs by lazy {
        getSharedPreferences("argus_prefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.divaneural.argus.R.layout.activity_config)

        // Bind seguro (checa null e evita crash)
        val apiBase = findViewById<EditText>(com.divaneural.argus.R.id.apiBase)
        val receiveBetas = findViewById<Switch>(com.divaneural.argus.R.id.receiveBetas)
        val btnSave = findViewById<Button>(com.divaneural.argus.R.id.btnSave)

        // Carrega valores previamente salvos (se existirem)
        val savedBase = prefs.getString("api_base", null)
        if (savedBase != null) apiBase?.setText(savedBase)

        val savedBetas = prefs.getBoolean("receive_betas", false)
        receiveBetas?.isChecked = savedBetas

        // Salvar
        btnSave?.setOnClickListener {
            val base = apiBase?.text?.toString()?.trim().orEmpty()
            val betas = receiveBetas?.isChecked ?: false

            prefs.edit()
                .putString("api_base", base)
                .putBoolean("receive_betas", betas)
                .apply()

            Toast.makeText(this, "Configurações salvas", Toast.LENGTH_SHORT).show()
            finish() // fecha a tela após salvar
        }
    }
}
