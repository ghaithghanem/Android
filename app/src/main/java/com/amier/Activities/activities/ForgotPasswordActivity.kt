package com.amier.Activities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.amier.Activities.api.Api
import com.amier.Activities.models.EMAIL
import com.amier.Activities.models.Email
import com.amier.Activities.models.ForgotAndToken
import com.amier.modernloginregister.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var verif_email: Button


    lateinit var forgotemail: EditText


    var loadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotemail = findViewById(R.id.forgotemail)

        verif_email = findViewById(R.id.verif_email)


        verif_email.setOnClickListener {
            var emaill = Email()
            var emailreset = forgotemail.text.toString()
            emaill.email = emailreset
            val apiuser = Api.create().forgotpassword(emaill)
            loadingDialog.LoadingDialog(this)
            apiuser.enqueue(object: Callback<ForgotAndToken>{
                override fun onResponse(
                    call: Call<ForgotAndToken>,
                    response: Response<ForgotAndToken>
                ) {
                    if(response.isSuccessful){

                        MotionToast.darkColorToast(
                            this@ForgotPasswordActivity,
                            "Good ",
                            "Next step",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this@ForgotPasswordActivity,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )

                        val intent = Intent(applicationContext, resetActivity::class.java)
                        intent.apply {
                            putExtra(EMAIL, emailreset)
                        }
                        startActivity(intent)


                    } else {

                        Toast.makeText(applicationContext, "uncorrect email ", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<ForgotAndToken>, t: Throwable) {
                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }

            })


        }


    }
}