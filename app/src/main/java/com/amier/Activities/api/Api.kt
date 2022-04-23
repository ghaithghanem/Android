package com.amier.Activities.api

import com.amier.Activities.Constants
import com.amier.Activities.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {
    @Multipart
    @POST("user")
    fun userSignUp(
        @PartMap data: LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part?
    ) : Call<User>

    @POST("user/login")
    fun userLogin(
        @Body user: User
    ):Call<User>

    @GET("user/oyoy/{id}")
    fun checkVerified(
        @Path("id") id: String,

    ):Call<User>

    @POST("user/Auth")
    fun userLoginSocial(
        @Body user: User
    ):Call<User>

    @POST("user/notif")
    fun sendNotif(
        @Body user: User
    ):Call<User>

    @POST("https://api-C2B86342-5275-4183-9F0C-28EF1E4B3014.sendbird.com/v3/users")
    @Headers("Api-Token: 9838c32272965383009ace17937bb8565e108d38")
    fun sendBirdCreate(
        @Body user: SendBirdUser
    ):Call<SendBirdUser>

    companion object {
        fun create() : Api {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
            return retrofit.create(Api::class.java)

        }
    }
}