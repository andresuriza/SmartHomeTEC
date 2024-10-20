package com.example.smarthometec

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("Users")
    //suspend fun getUsers(): Response<Users>
    suspend fun getUsers(): Response<List<Users>>

    @POST("Users/Register")
    suspend fun postRequest(@Body user: UserRegister): UserRegister
}