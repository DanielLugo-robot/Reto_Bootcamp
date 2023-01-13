package com.example.retobootcampmobilesophos2022.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.retobootcampmobilesophos2022.R
import com.example.retobootcampmobilesophos2022.view.prefs.UserApplication.Companion.prefs

class MenuActivity : AppCompatActivity() {

    lateinit var btnSendDocuments : Button
    lateinit var btnSeeDocuments : Button
    lateinit var btnOffices : Button
    lateinit var tvName : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnSendDocuments = findViewById(R.id.btnSendDocuments)
        btnSendDocuments.setOnClickListener {
            goToSendDocuments()
        }
        btnSeeDocuments = findViewById(R.id.btnViewDocuments)
        btnSeeDocuments.setOnClickListener {
            goToSeeDocuments()
        }
        btnOffices = findViewById(R.id.btnOfiices)
        btnOffices.setOnClickListener {
            goToSeeOffices()
        }
        val userName = prefs.getUserName()
        tvName = findViewById(R.id.textName)
        tvName.text = "$userName"
    }
    fun goToSendDocuments(){
        val i = Intent(this, SendDocuments :: class.java)
        startActivity(Intent(i))
    }
    fun goToSeeDocuments(){
        val i = Intent(this, SeeDocuments :: class.java)
        startActivity(Intent(i))
    }
    fun goToSeeOffices(){
        val i = Intent(this, SeeOffices :: class.java)
        startActivity(Intent(i))
    }
}