package com.amier.Activities.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amier.Activities.api.Api
import com.amier.Activities.models.SendBirdUser
import com.amier.Activities.models.User
import com.amier.Activities.storage.SharedPrefManager
import com.amier.modernloginregister.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sendbird.android.SendBird
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val RC_SIGN_IN = 123




class LoginActivity : AppCompatActivity() {
    lateinit var mSharedPref: SharedPreferences
    var loadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)

        SendBird.init("C2B86342-5275-4183-9F0C-28EF1E4B3014", this)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        if(mSharedPref.getString("email","")!!.isNotEmpty()){
            finish()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
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

            loadingDialog.LoadingDialog(this)
            loadingDialog.startLoadingDialog()
            val apiuser = Api.create().userLogin(userr)
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

        }

        btnRegLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            var userr = User()
            userr.email = account.email.toString()
            userr.nom = account.familyName.toString()
            userr.prenom = account.givenName.toString()
            Log.i("user google est : ",userr.toString()+account.photoUrl)
            loadingDialog.LoadingDialog(this)
            loadingDialog.startLoadingDialog()
            val apiuser = Api.create().userLoginSocial(userr)
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

                    } else if (response.code() == 404) {

                       createAccount(account.givenName.toString(),account.familyName.toString(),account.email.toString(),null,null)
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }else{
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
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Log.i("user google est : ",e.toString())
        }
    }

    override fun onStart() {
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        var ok = updateUI(account)
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
    fun createAccount(firstName: String, lastName: String, email: String, password: String?, number: String?){



        val apiInterface = Api.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["nom"] = RequestBody.create(MultipartBody.FORM, firstName)
        data["prenom"] = RequestBody.create(MultipartBody.FORM, lastName)
        data["email"] = RequestBody.create(MultipartBody.FORM, email)
        if(password != null){
            data["password"] = RequestBody.create(MultipartBody.FORM, password)
            data["numt"] = RequestBody.create(MultipartBody.FORM, number!!)
        }



            apiInterface.userSignUp(data,null).enqueue(object: Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if(response.isSuccessful){
                        mSharedPref.edit().apply{
                            putString("photoProfil", response.body()?.user?.photoProfil.toString())
                            putString("_id", response.body()?.user?._id.toString())
                            putString("nom", response.body()?.user?.nom.toString())
                            putString("email", response.body()?.user?.email.toString())
                            putString("numt", response.body()?.user?.numt.toString())
                            putString("prenom", response.body()?.user?.prenom.toString())
                            //putBoolean("session", true)
                        }.apply()
                        Log.i("signup good", response.body().toString())
                        var userSendbird = SendBirdUser()
                        userSendbird.nickname = response.body()?.user?.prenom.toString()
                        userSendbird.profile_url = response.body()?.user?.photoProfil.toString()
                        userSendbird.user_id = response.body()?.user?._id.toString()
                        Log.i("photo de profil sendbird : ", userSendbird.profile_url!!)
                        apiInterface.sendBirdCreate(userSendbird).enqueue(object:
                            Callback<SendBirdUser>{
                            override fun onResponse(
                                call: Call<SendBirdUser>,
                                response: Response<SendBirdUser>
                            ) {
                                Log.i("server reponse sendbird good: ",response.body().toString())
                            }
                            override fun onFailure(call: Call<SendBirdUser>, t: Throwable) {
                                Log.i("server reponse sendbird error: ",t.toString())
                            }
                        })
//                        showAlertDialog()
                    } else {
                        Log.i("signup error", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    progress_bar.progress = 0
                    println(t.toString())
                }

            })


    }
}
