package com.wonder.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fci_info.*
import kotlinx.android.synthetic.main.activity_main.*

class FciInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fci_info)

        regresar.setOnClickListener{
            val intent: Intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)

        }

        pdf.setOnClickListener{
            val pdf: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fv9-5.failiem.lv/down.php?cf&i=jsuara88m&n=Antecedentes+del+FCI+010-2021"))
            startActivity(pdf)
        }
    }
}