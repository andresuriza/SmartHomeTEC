package com.example.smarthometec

import com.google.gson.annotations.SerializedName

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

data class UserRegister (
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellidos")
    val apellidos: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("correoElectronico")
    val correoElectronico: String,
    @SerializedName("contrasena")
    val contrasena: String,
    @SerializedName("pedidos")
    val pedidos: List<Int>,
    @SerializedName("facturas")
    val facturas: List<Int>,
    @SerializedName("dispositivos")
    val dispositivos: List<Int>,
    @SerializedName("direccionesEntrega")
    val direccionesEntrega: List<Int>
)