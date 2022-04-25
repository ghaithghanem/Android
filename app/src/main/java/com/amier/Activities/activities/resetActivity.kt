package com.amier.Activities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.amier.Activities.api.Api
import com.amier.Activities.models.*
import com.amier.modernloginregister.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class resetActivity : AppCompatActivity() {
    lateinit var verif_token: Button
    lateinit var forgottoken: EditText
    //lateinit var forgotemail: EditText
    lateinit var newpass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        verif_token = findViewById(R.id.verif_token)
        forgottoken = findViewById(R.id.forgottoken)
        newpass = findViewById(R.id.newpass)


        verif_token.setOnClickListener {

         val verif1 = TokenReset()
            verif1.token = forgottoken.text.toString().toInt()
        val password1 = passreset()
            password1.Password = newpass.text.toString()
val email1 = Email()
            val name = intent.getStringExtra(EMAIL)
            email1.email = name
println( password1)
println(name)
            println(verif1)

            val apiuser = Api.create().resetpassword(name,forgottoken.text.toString().toInt(),password1)
            apiuser.enqueue(object: Callback<res> {
                override fun onResponse(
                    call: Call<res>,
                    response: Response<res>
                ) {
                    if(response.isSuccessful){


                        MotionToast.darkColorToast(
                            this@resetActivity,
                            "Good ",
                            "Success reset Password",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this@resetActivity,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )
                        val intent = Intent(applicationContext, LoginActivity::class.java)

                        startActivity(intent)


                    } else {

                        Toast.makeText(applicationContext, "uncorrect email ", Toast.LENGTH_LONG).show()
                        Log.i("PASSSSS_API_RESPONSE", response.toString())
                    }
                }

                override fun onFailure(call: Call<res>, t: Throwable) {
                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }

            })


        }
    }
}