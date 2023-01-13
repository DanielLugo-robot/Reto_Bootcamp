package com.example.retobootcampmobilesophos2022.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import com.example.retobootcampmobilesophos2022.R
import com.example.retobootcampmobilesophos2022.model.NewDocument
import com.example.retobootcampmobilesophos2022.viewModel.dataResponse
import com.example.retobootcampmobilesophos2022.viewModel.network.sophosApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor

class SendDocuments : AppCompatActivity(), OnItemSelectedListener {

    private val apiService: sophosApiService by lazy {
        sophosApiService.create()
    }

    lateinit var btnAddPhoto: ImageButton
    lateinit var btnBack: Button
    lateinit var btnFind_document: Button
    lateinit var btnSend: Button
    lateinit var etIdentificacion: EditText
    lateinit var etNombre: EditText
    lateinit var etApelldio: EditText
    lateinit var etCorreo: EditText
    lateinit var etTipoAdjunto: EditText
    lateinit var imageString: String
    lateinit var citySpinner: Spinner
    lateinit var arrayCity: ArrayAdapter<String>
    lateinit var citySelected: String
    lateinit var typeDocumentSpinner: Spinner
    lateinit var arrayTypeDocument: ArrayAdapter<String>
    lateinit var typeDocumentSelected: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_documents)

        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener { goBack() }
        btnAddPhoto = findViewById(R.id.add_photo)
        btnAddPhoto.setOnClickListener{ dispatchTakePictureIntent() }
        btnFind_document = findViewById(R.id.find_document)
        btnFind_document.setOnClickListener { openGallery() }
        btnSend = findViewById(R.id.sendBtn)
        btnSend.setOnClickListener{ sendDATA() }
        etIdentificacion = findViewById(R.id.document_num)
        etNombre = findViewById(R.id.txt_name)
        etApelldio = findViewById(R.id.txt_lastName)
        etCorreo = findViewById(R.id.txt_email)
        etTipoAdjunto = findViewById(R.id.type_attached)

        arrayCity = ArrayAdapter<String>(this, R.layout.spinner_items)
        arrayCity.addAll("Ciudad","Mexico","Bogotá","Medellín","Panamá", "Estados Unidos", "Chile")
        citySpinner = findViewById(R.id.spinnerCity)
        citySpinner.onItemSelectedListener = this
        citySpinner.adapter = arrayCity

        arrayTypeDocument = ArrayAdapter<String>(this, R.layout.spinner_items)
        arrayTypeDocument.addAll("Tipo de documento","CC","TI","PA","CE")
        typeDocumentSpinner = findViewById(R.id.spinnerTypeID)
        typeDocumentSpinner.onItemSelectedListener = this
        typeDocumentSpinner.adapter = arrayTypeDocument

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position > 0){
            citySelected = arrayCity.getItem(position).toString()
            typeDocumentSelected = arrayTypeDocument.getItem(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun sendDATA() {

        apiService.sendRequest(NewDocument(typeDocumentSelected, etIdentificacion.text.toString(),
            etNombre.text.toString(), etApelldio.text.toString(), citySelected,
            etCorreo.text.toString(), etTipoAdjunto.text.toString(), imageString)).enqueue(

            object:Callback<dataResponse>{
                override fun onResponse(
                    call: Call<dataResponse>,
                    response: Response<dataResponse>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext, "Registro Exitoso",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(applicationContext, "Debes llenar todos los campos",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<dataResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Se produjo error en el servidor", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun goBack(){

        val i = Intent(this, MenuActivity :: class.java)
        startActivity(Intent(i))
    }

    private fun bitmapToBase64(imageBitmap:Bitmap):String{

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes : ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)

    }

    // Take a photo
    val REQUEST_IMAGE_CAPTURE = 1

    @Suppress("DEPRECATION")
    private fun dispatchTakePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                Toast.makeText(applicationContext, "Accediste a la camara", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Select image from media storage
    val PHOTO_PICKER_REQUEST_CODE = 12

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Suppress("DEPRECATION")
    private fun openGallery(){
            Intent(MediaStore.ACTION_PICK_IMAGES).also { openGalleryIntent ->
                openGalleryIntent.resolveActivity(packageManager)?.also {
                    intent.type = "images/*"
                    startActivityForResult(openGalleryIntent, PHOTO_PICKER_REQUEST_CODE)
                }
            }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageString = bitmapToBase64(imageBitmap)
            Toast.makeText(applicationContext, "Tomaste una foto",Toast.LENGTH_LONG).show()
        }

        if (requestCode == PHOTO_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val imageUri : Uri? = data.data
            val parcelFileDescriptor = imageUri?.let { contentResolver.openFileDescriptor(it, "r") }
            val fileDescriptor : FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val imageBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            imageString = bitmapToBase64(imageBitmap)
            Toast.makeText(applicationContext, "Seleccionaste una imagen",Toast.LENGTH_LONG).show()
        }
    }

}


