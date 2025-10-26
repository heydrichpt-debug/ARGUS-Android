package com.divaneural.argus.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.divaneural.argus.R

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val apiBase = findViewById<EditText>(R.id.apiBase)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Lê valor salvo (ou usa o default do strings.xml)
        val sp = getSharedPreferences("argus_prefs", MODE_PRIVATE)
        val current = sp.getString("api_base", getString(R.string.default_api_base))
        apiBase.setText(current)

        btnSave.setOnClickListener {
            val value = apiBase.text.toString().trim()
            if (value.isEmpty()) {
                Toast.makeText(this, "Informe a URL da API", Toast.LENGTH_SHORT).show()
            } else {
                sp.edit().putString("api_base", value).apply()
                Toast.makeText(this, "Salvo", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
