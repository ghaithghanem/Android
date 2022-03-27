package com.amier.Activities.api

import com.amier.Activities.models.DefaultResponse
import com.amier.Activities.models.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {
    @Multipart
    @POST("")
    fun createUser(
        @Part photoProfil: MultipartBody.Part,
        @Part("email") email: MultipartBody.Part,
        @Part("nom") name: MultipartBody.Part,
        @Part("prenom") name1: MultipartBody.Part,
        @Part("password") password: MultipartBody.Part,
        @Part("numt") num: RequestBody
    ):Call<DefaultResponse>
    companion object {
        operator fun invoke(): Api {
            return Retrofit.Builder()
                .baseUrl("https://lost-and-found-back.herokuapp.com/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email")email: String,
        @Field("password")password: String
    ):Call<LoginResponse>
}