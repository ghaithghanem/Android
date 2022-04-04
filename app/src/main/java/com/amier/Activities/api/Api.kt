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
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part
    ) : Call<userSignUpResponse>

    @POST("user/login")
    fun userLogin(
        @Body user: User
    ):Call<UserAndToken>


    @GET("article")
    fun GetAllArticles():Call<ArticlesReponse>

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