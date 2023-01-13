package com.example.retobootcampmobilesophos2022.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retobootcampmobilesophos2022.R
import com.example.retobootcampmobilesophos2022.model.DocumentResponse
import com.example.retobootcampmobilesophos2022.model.Documents
import com.example.retobootcampmobilesophos2022.view.prefs.UserApplication.Companion.prefs
import com.example.retobootcampmobilesophos2022.viewModel.Adapter
import com.example.retobootcampmobilesophos2022.viewModel.network.sophosApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SeeDocuments : AppCompatActivity() {

    private val apiService: sophosApiService by lazy {
        sophosApiService.create()
    }

    lateinit var btnBack : Button
    lateinit var ivR : ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_documents)

        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener { goBack() }
        ivR = findViewById(R.id.ivR)

        val email = prefs.getEmail()
        val recyclerView : RecyclerView = findViewById(R.id.recyclerViewList)

        apiService.getDocuments(email).enqueue(
            object: Callback<Documents> {override fun onResponse(
                    call: Call<Documents>,
                    response: Response<Documents>
                ) {
                    if(response.isSuccessful){
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(this@SeeDocuments)
                            val adapter = Adapter(response.body()!!){
                                val call = apiService.getDataByIdRegister(it.IdRegistro)
                                call.enqueue(object : Callback<DocumentResponse>{
                                    override fun onResponse(
                                        call: Call<DocumentResponse>,
                                        response: Response<DocumentResponse>
                                    ) {
                                        if(response.isSuccessful){
                                            val responseBody = response.body()
                                            responseBody!!.Items.forEach { item ->
                                                val image = decodeImage(item.Adjunto)
                                                ivR.setImageBitmap(image)
                                            }
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<DocumentResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e("error", t.message.toString())
                                    }
                                })
                            }
                            recyclerView.adapter = adapter
                        }
                    }
                }

                override fun onFailure(call: Call<Documents>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("error", t.message.toString())
                }
            }
        )
    }

    fun goBack() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(Intent(i))
    }

    fun decodeImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}



