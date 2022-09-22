package com.wonder.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        Handler().postDelayed(Runnable() {
            run(){

               val preferences:SharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)
               var editor = preferences.edit()
               var bandera = preferences.getString("bandera", "0")?.toInt()

                if (bandera == 1){
                    val intent:Intent = Intent( this@SplashActivity, MainActivity::class.java )
                    startActivity(intent)
                }else{
                    editor.putString("bandera","1").commit()
                    val intent:Intent = Intent( this@SplashActivity, MainActivity::class.java )
                    startActivity(intent)

                    val intent2:Intent = Intent( this@SplashActivity, SliderActivity::class.java )
                    startActivity(intent2)
                }

               finish()
            }
        },3000)
    }
}