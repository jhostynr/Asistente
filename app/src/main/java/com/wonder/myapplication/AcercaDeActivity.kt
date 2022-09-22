package com.wonder.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_acerca_de.*
import kotlinx.android.synthetic.main.activity_estadisticas.*
import kotlinx.android.synthetic.main.activity_fci_info.*
import kotlinx.android.synthetic.main.activity_fci_info.regresar

class AcercaDeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)

        regresar.setOnClickListener{
            val intent: Intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)

        }
        idUniversidad.setOnClickListener{
            val pdf: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ug.edu.ec/"))
            startActivity(pdf)
        }
    }
}