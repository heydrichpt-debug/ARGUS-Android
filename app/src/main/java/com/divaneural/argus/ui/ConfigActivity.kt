package com.divaneural.argus.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.divaneural.argus.R
import com.divaneural.argus.util.Prefs

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val apiBase = findViewById<EditText>(R.id.apiBase)
        val adminUser = findViewById<EditText>(R.id.adminUser)
        val adminPass = findViewById<EditText>(R.id.adminPass)
        val receiveBetas = findViewById<CheckBox>(R.id.receiveBetas)
        val btnSave = findViewById<Button>(R.id.btnSave)

        apiBase.setText(Prefs.getApiBase(this))
        adminUser.setText(Prefs.getAdminUser(this))
        adminPass.setText(Prefs.getAdminPass(this))
        receiveBetas.isChecked = Prefs.getReceiveBetas(this)

        btnSave.setOnClickListener {
            Prefs.setApiBase(this, apiBase.text.toString().ifBlank { getString(R.string.default_api_base) })
            Prefs.setAdminUser(this, adminUser.text.toString())
            Prefs.setAdminPass(this, adminPass.text.toString())
            Prefs.setReceiveBetas(this, receiveBetas.isChecked)
            Toast.makeText(this, "Configurações salvas", Toast.LENGTH_SHORT).show()
        }
    }
}
