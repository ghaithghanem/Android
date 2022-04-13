package com.amier.Activities.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.amier.Activities.api.Api
import com.amier.Activities.models.User
import com.amier.Activities.models.UserAndToken
import com.amier.Activities.storage.SharedPrefManager
import com.amier.modernloginregister.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var remember_me: CheckBox
    lateinit var mSharedPref: SharedPreferences
    lateinit var gotoRegister: Button
    lateinit var forgotPassword: Button
    var loadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {

        //mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
      // if(mSharedPref.getString("email","")!!.isNotEmpty()){
           //finish()
           //val intent = Intent(applicationContext, HomeActivity::class.java)
           //startActivity(intent)
      // }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        remember_me = findViewById(R.id.remember);
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        gotoRegister = findViewById(R.id.gotoRegister)
        forgotPassword = findViewById(R.id.forgotPassword)


        forgotPassword.setOnClickListener {
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
            startActivity(intent)

        }

        gotoRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {


            var userr = User()
            userr.email = editTextEmail.text.toString()
            userr.password = editTextPassword.text.toString()
            val apiuser = Api.create().userLogin(userr)
            loadingDialog.LoadingDialog(this)
            apiuser.enqueue(object: Callback<UserAndToken>{
                override fun onResponse(call: Call<UserAndToken>, response: Response<UserAndToken>) {
                    if(response.isSuccessful){

                        Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                        Log.i("login User:", response.body().toString())
                        //Store company data in sharedpref
                        //mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                        mSharedPref.edit().apply{
                            if(remember_me.isChecked){
                                putBoolean("remember", true)
                                putString("email", userr.email)
                                putString("password", userr.password)
                            }
                            putString("photoProfil", response.body()?.user?.photoProfil.toString())
                            putString("id", response.body()?.user?._id.toString())
                            putString("nom", response.body()?.user?.nom.toString())
                            putString("email", userr.email)
                            putString("password", userr.password)
                            putString("numt", response.body()?.user?.numt.toString())
                            putString("prenom", response.body()?.user?.prenom.toString())

                            println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                            println(response.body()?.user?.password.toString())
                            //putBoolean("session", true)
                        }.apply()
                        loadingDialog.dismissDialog()
                        finish()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        loadingDialog.dismissDialog()
                        Toast.makeText(applicationContext, "uncorrect email or password", Toast.LENGTH_LONG).show()
                        Log.i("RETROFIT_API_RESPONSE", response.toString())
                        Log.i("login response", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<UserAndToken>, t: Throwable) {
                    loadingDialog.dismissDialog()
                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }

            })
            loadingDialog.startLoadingDialog()
            println("/////////////////////")
            println(mSharedPref.getString("photoProfil", "user.email").toString())

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

        }


    }

    override fun onStart() {
        super.onStart()
        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    private fun navigate(){
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }

}
