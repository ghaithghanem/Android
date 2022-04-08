package com.amier.Activities.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.amier.Activities.api.Api
import com.amier.Activities.models.User
import com.amier.Activities.storage.SharedPrefManager
import com.amier.modernloginregister.R
import com.sendbird.android.SendBird
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    var loadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        SendBird.init("C2B86342-5275-4183-9F0C-28EF1E4B3014", this)

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        if(mSharedPref.getString("email","")!!.isNotEmpty()){
            finish()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.setOnClickListener {
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
            var userr = User()
            userr.email = editTextEmail.text.toString()
            userr.password = editTextPassword.text.toString()
            val apiuser = Api.create().userLogin(userr)
            loadingDialog.LoadingDialog(this)
            apiuser.enqueue(object: Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        Log.i("login User:", response.body().toString())
                        //Store company data in sharedpref
                        //mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                        mSharedPref.edit().apply{
                            putString("photoProfil", response.body()?.user?.photoProfil.toString())
                            putString("_id", response.body()?.user?._id.toString())
                            putString("nom", response.body()?.user?.nom.toString())
                            putString("email", response.body()?.user?.email.toString())
                            putString("numt", response.body()?.user?.numt.toString())
                            putString("prenom", response.body()?.user?.prenom.toString())
                            //putBoolean("session", true)
                        }.apply()
                        Log.i("shared pref id user : ",mSharedPref.getString("nom","")!!)
                        if(connectToSendBird(mSharedPref.getString("_id","")!!)){
                            Log.i("sendbird connecté :","")
                        }else{
                            Log.i("sendbird moch connecté :","")
                        }
                        loadingDialog.dismissDialog()
                        finish()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        loadingDialog.dismissDialog()
                            Toast.makeText(applicationContext, "Email ou Mot de passe incorrect", Toast.LENGTH_LONG).show()
                        Log.i("RETROFIT_API_RESPONSE", response.toString())
                        Log.i("login response", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    loadingDialog.dismissDialog()
                    Toast.makeText(applicationContext, "Erreur server", Toast.LENGTH_LONG).show()
                }

            })
            loadingDialog.startLoadingDialog()
        }

        btnRegLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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
    fun connectToSendBird(userID: String) :Boolean{
        var a = false
        SendBird.connect(userID) { user, e ->
            if (e != null) {
                Log.i("erreur connecting sendbird : ",e.toString())
            }else{
                a = true
            }
        }
        return  a
    }
}
