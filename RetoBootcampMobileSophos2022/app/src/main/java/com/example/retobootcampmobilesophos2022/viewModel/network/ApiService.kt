package com.example.retobootcampmobilesophos2022.viewModel.network

import com.example.retobootcampmobilesophos2022.model.*
import com.example.retobootcampmobilesophos2022.viewModel.dataResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface sophosApiService{

    @GET("RS_Usuarios")
    fun login(
        @Query("idUsuario") idUsuario: String,
        @Query("clave") clave: String
    ): Call<Users>

    @POST("RS_Documentos")
    fun sendRequest(
        @Body newDocument: NewDocument
    ): Call<dataResponse>

    @GET("RS_Documentos")
    fun getDocuments(
        @Query("correo") correo: String
    ): Call<Documents>

    @GET("RS_Documentos")
    fun getDataByIdRegister(
        @Query("idRegistro") idRegistro: String
    ): Call<DocumentResponse>

    @GET("RS_Oficinas")
    fun getOffices(): Call<Offices>

    companion object Factory{
        private const val BASE_URL =
            "https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/"
        fun create(): sophosApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(sophosApiService::class.java)
        }
    }
}
