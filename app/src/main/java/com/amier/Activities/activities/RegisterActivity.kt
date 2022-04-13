    package com.amier.Activities.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.amier.Activities.api.Api
import com.amier.Activities.models.userSignUpResponse
import com.amier.modernloginregister.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class RegisterActivity : AppCompatActivity(){
    private var selectedImageUri: Uri? = null
 var imagePicker: ImageView?=null
private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        imagePicker = findViewById(R.id.profileimage)
         fab = findViewById(R.id.floatingActionButton)
       fab.setOnClickListener(View.OnClickListener {
           ImagePicker.with(this)
               .crop()	    			//Crop image(Optional), Check Customization for more option
               .compress(1024)			//Final image size will be less than 1 MB(Optional)
               .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
               .start()
       })
        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val name1 = editTextName1.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val num = editTextNum.text.toString().trim()



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
            if(name.isEmpty()){
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }
            if(name1.isEmpty()){
                editTextName.error = "Last name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }
            if(num.isEmpty()){
                editTextName.error = "Num required"
                editTextName.requestFocus()
                return@setOnClickListener
            }
            if (selectedImageUri == null) {
                layout_root.snackbar("Select an Image First")
                return@setOnClickListener
            }

            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return@setOnClickListener
            login(
                name,name1,
                email,
                password,
                num
            )
            print(parcelFileDescriptor);
//            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//
//            val outputStream = FileOutputStream(file)
//            inputStream.copyTo(outputStream)
//            progress_bar.progress = 0
//            val body = UploadRequestBody(file, "photoProfil", this)
//            Api().createUser(
//                MultipartBody.Part.createFormData("photoProfil", file.name,body),
//                MultipartBody.Part.createFormData("nom", name,body),
//                MultipartBody.Part.createFormData("prenom", name1,body),
//                MultipartBody.Part.createFormData("password", password,body),
//                MultipartBody.Part.createFormData("numt", num,body),
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
//                ).enqueue(object: Callback<DefaultResponse>{
//                override fun onResponse(
//                    call: Call<DefaultResponse>,
//                    response: Response<DefaultResponse>
//                ) {
//                    Toast.makeText(applicationContext, response.body()?.reponse,Toast.LENGTH_LONG).show()
//                    progress_bar.progress = 100
//                }
//
//                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                    Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG).show()
//                    progress_bar.progress = 0
//                }
//            })
        }
    }
    private fun login(firstName: String, lastName: String, email: String, password: String, number: String){

        if(selectedImageUri == null){
            println("image null")

            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        println("-------------------------------------"+stream)
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
        data["password"] = RequestBody.create(MultipartBody.FORM, password)
        data["numt"] = RequestBody.create(MultipartBody.FORM, number)

        if (profilePicture != null) {
            println("++++++++++++++++++++++++++++++++++++"+profilePicture)
            apiInterface.userSignUp(data,profilePicture).enqueue(object:
                Callback<userSignUpResponse> {
                override fun onResponse(
                    call: Call<userSignUpResponse>,
                    response: Response<userSignUpResponse>
                ) {
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())
                        showAlertDialog()
                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<userSignUpResponse>, t: Throwable) {
                    progress_bar.progress = 0
                    println("noooooooooooooooooo")
                }

            })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
             imagePicker?.setImageURI(selectedImageUri)

        }
    }
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("Thank you for choosing showapp.tn! \n We have sent you an email to confirm your account")
            .setPositiveButton("Ok") {dialog, which ->
                showSnackbar("welcome")
            }
            .show()
    }
    private fun showSnackbar(msg: String) {
        Snackbar.make(layout_root, msg, Snackbar.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }



}
