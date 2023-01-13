package com.example.retobootcampmobilesophos2022.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.retobootcampmobilesophos2022.R
import com.example.retobootcampmobilesophos2022.model.Users
import com.example.retobootcampmobilesophos2022.view.prefs.UserApplication.Companion.prefs
import com.example.retobootcampmobilesophos2022.viewModel.network.sophosApiService
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private val apiService:sophosApiService by lazy {
        sophosApiService.create()
    }

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var btnBiometricLogin : Button
    lateinit var btnMenu : Button
    lateinit var etImputEmail : EditText
    lateinit var etImputPassword : EditText
    lateinit var btnShowHidePassword : ImageButton

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etImputEmail = findViewById(R.id.input_email)
        etImputPassword = findViewById(R.id.input_password)
        btnMenu = findViewById(R.id.btn_login)
        btnBiometricLogin = findViewById(R.id.btn_fingerPrint)
        btnShowHidePassword = findViewById(R.id.btn_ShowHide)
        btnShowHidePassword.setOnClickListener {
            etImputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            btnShowHidePassword.setBackgroundResource(R.drawable.ic_baseline_visibility_24)
        }
        btnMenu.setOnClickListener {
            login()
            accesToDetail()
        }
        checkLogin()
    }

    fun goToMenu(){
        val i = Intent(this, MenuActivity :: class.java)
        startActivity(Intent(i))
    }

    private fun accesToDetail(){
        if(etImputEmail.text.toString().isNotEmpty()){
            prefs.saveEmail(etImputEmail.text.toString())
        }
    }

    private fun checkLogin(){
        val access = prefs.getAccess()
        if(access){
            biometric()
        }else{
            btnBiometricLogin.setOnClickListener {
                Toast.makeText(
                    applicationContext, "Usa tus crededenciales para ingresar con huella", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun login (){

        val call = apiService.login(etImputEmail.text.toString(), etImputPassword.text.toString())
        call.enqueue(object: retrofit2.Callback<Users>{

            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if(loginResponse.acceso){
                            goToMenu()
                            prefs.saveName(loginResponse.nombre).toString()
                            prefs.saveAccess(loginResponse.acceso)
                            Toast.makeText(applicationContext, "Iniciaste sesión",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext, "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(applicationContext, "Ingresa tu usuario y contraseña para acceder", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo error en el servidor", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun biometric(){
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                goToMenu()
                Toast.makeText(applicationContext, "Iniciaste sesión", Toast.LENGTH_SHORT).show()
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Fallo en la autenticación ", Toast.LENGTH_SHORT).show()
            }
        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Toca el sensor")
            .setDescription("Pon tu dedo en el sensor para autenticarte")
            .setNegativeButtonText("CANCELAR")
            .build()

        btnBiometricLogin.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }
    }
}

