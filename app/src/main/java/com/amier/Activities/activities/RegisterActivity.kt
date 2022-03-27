package com.amier.Activities.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.amier.Activities.api.Api
import com.amier.Activities.models.DefaultResponse
import com.amier.modernloginregister.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity(), UploadRequestBody.UploadCallback{
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
                editTextName.error = "Name required"
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

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))

            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            progress_bar.progress = 0
            val body = UploadRequestBody(file, "photoProfil", this)
            Api().createUser(
                MultipartBody.Part.createFormData("photoProfil", file.name,body),
                MultipartBody.Part.createFormData("nom", name,body),
                MultipartBody.Part.createFormData("prenom", name1,body),
                MultipartBody.Part.createFormData("password", password,body),
                MultipartBody.Part.createFormData("numt", num,body),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
                ).enqueue(object: Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    Toast.makeText(applicationContext, response.body()?.reponse,Toast.LENGTH_LONG).show()
                    progress_bar.progress = 100
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG).show()
                    progress_bar.progress = 0
                }
            })
        }
    }
    private fun uploadImage() {

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
             imagePicker?.setImageURI(selectedImageUri)

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun onProgressUpdate(percentage: Int) {
        progress_bar.progress = percentage
    }

}
