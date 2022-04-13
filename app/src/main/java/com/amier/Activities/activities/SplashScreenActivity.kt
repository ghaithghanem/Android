package com.amier.Activities.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.amier.Activities.api.Api
import com.amier.Activities.models.User
import com.amier.Activities.models.UserAndToken
import com.amier.modernloginregister.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


lateinit var mSharedPref: SharedPreferences
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        supportActionBar?.hide();
        lateinit var handler: Handler
        //-------------------------------------------------------
        var user = User()
        val email: String = mSharedPref.getString("email", "zwayten").toString()
        val password: String = mSharedPref.getString("password", "zwayten").toString()

        user.email = email
        user.password = password
        var remember: Boolean = false
        remember = mSharedPref.getBoolean("remember", false)
        Log.i("hedha mail : ",email);
        val apiuser = Api.create().userLogin(user)
println(remember)

        apiuser.enqueue(object: Callback<UserAndToken> {
            override fun onResponse(call: Call<UserAndToken>, response: Response<UserAndToken>) {
                println("/////////////////////////////////////////////55555")
                println(response)
                if(response.isSuccessful && remember == true){

                    print(email);
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    println(response.code())
                } else if(!response.isSuccessful || remember == false){

                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    println(response.code())
                }
            }

            override fun onFailure(call: Call<UserAndToken>, t: Throwable) {

                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
            }

        })
    }
}