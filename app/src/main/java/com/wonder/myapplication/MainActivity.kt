package com.wonder.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardChat.setOnClickListener{
            //val intent: Intent = Intent(this, ChatActivity:: class.java)
            //startActivity(intent)

            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setMessage("Nuestro asistente virtual es netamente informativo \n" +
                    "\n Puedes comenzar escribiendo el numero o la frase respectiva al tema de interés. \n" +
                    "\n" +
                    "1. Protocolos de seguridad \n" +
                    "2. Descanso \n" +
                    "3. Actividad física\n" +
                    "4. Alimentación\n" +
                    "5. Bebidas \n" +
                    "0. Otro " +
                    "\n ¿Desea Continuar?")
                .setPositiveButton("SI", DialogInterface.OnClickListener { dialog, id ->
                    val intent: Intent = Intent(this, ChatActivity:: class.java)
                    startActivity(intent)
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                } )
            alert.show()
        }

        cardInformacion.setOnClickListener{
            val intent: Intent = Intent(this, SliderActivity:: class.java)
            startActivity(intent)
        }
        cardFci.setOnClickListener{
            val intent: Intent = Intent(this, FciInfoActivity:: class.java)
            startActivity(intent)
        }
        cardEstadistica.setOnClickListener{
            val intent: Intent = Intent(this, EstadisticasActivity:: class.java)
            startActivity(intent)
        }
        cardAcercaDe.setOnClickListener{
            val intent: Intent = Intent(this, AcercaDeActivity:: class.java)
            startActivity(intent)
        }
    }
}