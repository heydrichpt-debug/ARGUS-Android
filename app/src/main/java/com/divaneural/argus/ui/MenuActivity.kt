package com.divaneural.argus.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.divaneural.argus.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val grid = findViewById<LinearLayout>(R.id.grid)
        val labels = listOf("Downloads","Mãe","Filhos","Saúde","Logs","Atualizações","Config","Sobre")
        labels.chunked(2).forEach { row ->
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0,0,0,12)
            }
            row.forEach { label ->
                val tile = TextView(this).apply {
                    setBackgroundColor(resources.getColor(R.color.menu_tile_bg, theme))
                    setTextColor(resources.getColor(R.color.menu_text, theme))
                    text = label; textSize = 16f; setPadding(24,24,24,24)
                    val p = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    p.marginEnd = 12; layoutParams = p
                }
                rowLayout.addView(tile)
            }
            grid.addView(rowLayout)
        }
    }
}
