package com.amier.Activities.activities

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.amier.Activities.api.Api
import com.amier.Activities.models.userSignUpResponse
import com.amier.Activities.models.userUpdateResponse
import com.amier.Activities.storage.SharedPrefManager
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.activity_update.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class UpdateActivity : AppCompatActivity() {
    lateinit var _id: String
    lateinit var Name: EditText
    lateinit var Name1: EditText
    lateinit var Email: EditText
    lateinit var Num: EditText
    lateinit var profileimage1: ImageView
    lateinit var mSharedPref: SharedPreferences
    private var selectedImageUri: Uri? = null
    var imagePicker: ImageView?=null
    private lateinit var fb: FloatingActionButton
    private lateinit var buttonUpdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        mSharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)
        profileimage1 = findViewById<ImageView?>(R.id.profileimage1)
        val picStr: String = mSharedPref.getString("photoProfil", "").toString()
        Glide.with(this).load(Uri.parse(picStr)).into(profileimage1)

        val emailStr: String = mSharedPref.getString("nom", "user.email").toString()
        Name = findViewById<EditText?>(R.id.Name)as EditText
        Name.setText(emailStr)

        val prenomStr: String = mSharedPref.getString("prenom", "user.email").toString()
        Name1 = findViewById<EditText?>(R.id.Name1)as EditText
        Name1.setText(prenomStr)

        val nameStr: String = mSharedPref.getString("email", "user.email").toString()
        Email = findViewById<EditText?>(R.id.Email)as EditText
        Email.setText(nameStr)

        val phoneStr: String = mSharedPref.getString("numt", "user.email").toString()
        Num = findViewById<EditText?>(R.id.Num)as EditText
        Num.setText(phoneStr)


        buttonUpdate = findViewById(R.id.buttonUpdate)
        imagePicker = findViewById(R.id.profileimage1)
        fb = findViewById(R.id.fb)
        fb.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })
        buttonUpdate.setOnClickListener {

            val name = Name.text.toString().trim()
            val name1 = Name1.text.toString().trim()
            val email = Email.text.toString().trim()
            val num = Num.text.toString().trim()


            if(email.isEmpty()){
                Email.error = "Email required"
                Email.requestFocus()
                return@setOnClickListener
            }

            if(name.isEmpty()){
                Name.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }
            if(name1.isEmpty()){
               Name1.error = "Last name required"
                Name1.requestFocus()
                return@setOnClickListener
            }
            if(num.isEmpty()){
                Num.error = "Num required"
                Num.requestFocus()
                return@setOnClickListener
            }
            if (selectedImageUri == null) {
                layout_root.snackbar("Select an Image First")
                return@setOnClickListener
            }


            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return@setOnClickListener
           _id = mSharedPref.getString("id", "user.id").toString()
            println("66666666666666666666666666666")

println(_id)

            update(
                name,name1,
                email,
                num,
                _id
            )
            print(parcelFileDescriptor);

        }
    }

    private fun update(firstName: String, lastName: String, email: String, number: String,id: String){

        if(selectedImageUri == null){
            println("image null")

            return
        }

        val id1 = _id;
        //id1 = mSharedPref.getString("id", "user.id").toString();
        println("9999999999999999999999999999999999999999999")
        println(id1);
        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
        val profilePicture = request?.let {
            MultipartBody.Part.createFormData(
                "photoProfil",
                "image.jpeg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")


        val apiInterface = Api.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["nom"] = RequestBody.create(MultipartBody.FORM, firstName)
        data["prenom"] = RequestBody.create(MultipartBody.FORM, lastName)
        data["email"] = RequestBody.create(MultipartBody.FORM, email)
        data["numt"] = RequestBody.create(MultipartBody.FORM, number)

        if (profilePicture != null) {
            apiInterface.userUpdate(_id,data,profilePicture).enqueue(object:
                Callback<userUpdateResponse> {
                override fun onResponse(
                    call: Call<userUpdateResponse>,
                    response: Response<userUpdateResponse>
                ) {
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())

                        MotionToast.darkColorToast(
                            this@UpdateActivity,
                            "Good ",
                            "Success Update",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this@UpdateActivity,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )

                        mSharedPref.edit().apply{

                            putString("photoProfil", response.body()?.user?.photoProfil.toString())
                            putString("id", response.body()?.user?._id.toString())
                            putString("nom", response.body()?.user?.nom.toString())
                            putString("email", response.body()?.user?.email)
                            putString("password", response.body()?.user?.password)
                            putString("numt", response.body()?.user?.numt.toString())
                            putString("prenom", response.body()?.user?.prenom.toString())

                            println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                            println(response.body()?.user?.password.toString())
                            //putBoolean("session", true)
                        }.apply()

                        finish()
                        val intent = Intent(applicationContext, HomeActivity::class.java)

                        startActivity(intent)

                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<userUpdateResponse>, t: Throwable) {
                    progress_bar1.progress = 0
                    println("noooooooooooooooooo")
                }

            })
        }
    }
    override fun onStart() {
        super.onStart()
        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, LastFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
            imagePicker?.setImageURI(selectedImageUri)

        }
    }

}


private fun Unit.enqueue(callback: Callback<userUpdateResponse>) {

}
