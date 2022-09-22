package com.wonder.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class SliderActivity : AppCompatActivity() {

    // on below line we are creating a
    // variable for our view pager
    lateinit var viewPager: ViewPager

    // on below line we are creating a variable
    // for our slider adapter and slider list
    lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderList: ArrayList<SliderData>

    // on below line we are creating a variable for our
    // skip button, slider indicator text view for 3 dots
    lateinit var skipBtn: Button
    lateinit var backBtn: Button
    lateinit var indicatorSlideOneTV: TextView
    lateinit var indicatorSlideTwoTV: TextView
    lateinit var indicatorSlideThreeTV: TextView
    lateinit var indicatorSlideFourTV: TextView
    lateinit var indicatorSlideFiveTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        // on below line we are initializing all
        // our variables with their ids.
        viewPager = findViewById(R.id.idViewPager)
        skipBtn = findViewById(R.id.idBtnSkip)
        backBtn = findViewById(R.id.idBtnBack)
        indicatorSlideOneTV = findViewById(R.id.idTVSlideOne)
        indicatorSlideTwoTV = findViewById(R.id.idTVSlideTwo)
        indicatorSlideThreeTV = findViewById(R.id.idTVSlideThree)
        indicatorSlideFourTV = findViewById(R.id.idTVSlideFour)
        indicatorSlideFiveTV = findViewById(R.id.idTVSlideFive)

        // on below line we are adding click listener for our skip button
        skipBtn.setOnClickListener {
            // on below line we are opening a new activity
            val i = Intent(this@SliderActivity, MainActivity::class.java)
            startActivity(i)
        }
        backBtn.setOnClickListener {
            val intent1:Intent = Intent(Intent.ACTION_MAIN)
            intent1.addCategory(Intent.CATEGORY_HOME)
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent1)
        }

        // on below line we are initializing our slider list.
        sliderList = ArrayList()

        // on below line we are adding data to our list
        sliderList.add(
            SliderData(
                "Introduccción",
                "Esta aplicación nace del Proyecto FCI De la Universidad de Guayaquil.\n " +
                        "La cual consta de un Asistente Virtual únicamente informativo \n " +
                        "acerca de los habitos alimenticios frente al COvid-19 . \n " +
                        "Y varias de opciones en las cuales puede encontrar información acerca del COVID-19.",
                R.drawable.uglogo
            )
        )

        sliderList.add(
            SliderData(
                "Asistente Virtual",
                "Los asistentes virtuales son un software de soporte, orientado a proporcionar ayuda a los usuarios para realizar tareas, automatizarlas o aportar información.",
                R.drawable.bot
            )
        )

        sliderList.add(
            SliderData(
                "Covid-19",
                "Segun la OMS, la enfermedad por coronavirus (COVID-19) es una enfermedad respiratoria infecciosa causada por el virus SARS-CoV-2.",
                R.drawable.virus
            )
        )

        sliderList.add(
            SliderData(
                "Sugerencias",
                "Para una mejor interacción con el Asistente Virtual puede comenzar enviandole los siguiente numeros correspondientes a las siguientes opciones: \n" +
                        "1. Protocolos de seguridad\n" +
                        "2. Descanso/Sueño             \n" +
                        "3. Actividad física                \n" +
                        "4. Alimentación                    \n" +
                        "5. Bebidas                              ",
                R.drawable.sugerencia
            )
        )

        sliderList.add(
            SliderData(
                "Acuerdo",
                "Esta aplicación como sus componentes (Asistente Virtual) son unicamente de manera informativa \n" +
                        "Si está usted de acuerdo por favor precione el botón Aceptar, \n" +
                        "Caso contratio precione el botón Cerrar.",
                R.drawable.tos
            )
        )

        // on below line we are adding slider list
        // to our adapter class.
        sliderAdapter = SliderAdapter(this, sliderList)

        // on below line we are setting adapter
        // for our view pager on below line.
        viewPager.adapter = sliderAdapter

        // on below line we are adding page change
        // listener for our view pager.
        viewPager.addOnPageChangeListener(viewListener)

    }

    // creating a method for view pager for on page change listener.
    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            // we are calling our dots method to
            // change the position of selected dots.

            // on below line we are checking position and updating text view text color.
            if (position == 0) {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFiveTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.white))

            } else if (position == 1) {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFiveTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
            } else if (position == 2) {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFiveTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
            } else if (position == 3) {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideFiveTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
            }else {
                indicatorSlideTwoTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideThreeTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFourTV.setTextColor(resources.getColor(R.color.grey))
                indicatorSlideFiveTV.setTextColor(resources.getColor(R.color.white))
                indicatorSlideOneTV.setTextColor(resources.getColor(R.color.grey))
            }
        }

        // below method is use to check scroll state.
        override fun onPageScrollStateChanged(state: Int) {}
    }
}