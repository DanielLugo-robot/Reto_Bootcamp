package com.example.retobootcampmobilesophos2022.model

data class Document(

    val IdRegistro:String,
    val Fecha:String,
    val TipoId:String,
    val Identificacion:String,
    val Nombre:String,
    val Apellido:String,
    val Ciudad:String,
    val Correo:String,
    val TipoAdjunto:String,
    val Adjunto:String

)

data class DocumentResponse(
    val Items: List<Document>
)
