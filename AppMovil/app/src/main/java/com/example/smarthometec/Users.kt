package com.example.smarthometec

import com.google.gson.annotations.SerializedName

// Define clase de usuario para Json
data class Users (
    val apellidos: String,
    val contrasena: String,
    val correoElectronico: String,
    val direccionesEntrega: List<Any>,
    val dispositivos: List<Any>,
    val facturas: List<Any>,
    val id: Int,
    val nombre: String,
    val pedidos: List<Any>,
    val region: String
)

data class Producto(
    val distribuidorCedula: String,
    val id: Int,
    val nombre: String,
    val numeroSerieDispositivo: String,
    val precio: Double
)