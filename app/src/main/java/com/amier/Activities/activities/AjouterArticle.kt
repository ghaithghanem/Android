package com.amier.Activities.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.amier.Activities.api.Api
import com.amier.Activities.api.ApiArticle
import com.amier.Activities.models.Articles
import com.amier.Activities.models.Question
import com.amier.Activities.models.userSignUpResponse
import com.amier.modernloginregister.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_ajouter_article.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AjouterArticle : AppCompatActivity() {
    lateinit var mSharedPref: SharedPreferences
    private var selectedImageUri: Uri? = null
    var imagePicker: ImageView?=null
    var questionnn :String?=null
    var ajoutQuestion :Button?=null
    var valider :Button?=null
    var type:String="Lost"

    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_ajouter_article)
        imagePicker = findViewById(R.id.imageArticle)
        fab = findViewById(R.id.cameraArticle)
         ajoutQuestion = findViewById<Button>(R.id.ajoutQuestion)
         valider = findViewById<Button>(R.id.AjouterArticleButton)

        fab.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })
        ajoutQuestion!!.setOnClickListener {
            showdialog()
        }


        valider!!.setOnClickListener {
            val titre = titreArticle.text.toString().trim()
            val description = editTextTextMultiLine3.text.toString().trim()
            if(selectedImageUri == null || titre.isEmpty() ||description.isEmpty()){
                Toast.makeText(applicationContext, "Tout les champs sont obligatoire", Toast.LENGTH_LONG).show()
            }else{

                if(switch1.isActivated){
                    type="Found"
                }else{
                    type="Lost"
                }
                var questionEnvoye = ""
                if(questionnn!=null){
                    questionEnvoye = questionnn.toString()
                }
                AjoutArticle(titre,questionEnvoye,description,type,mSharedPref.getString("_id","")!!)
            }

        }

    }



    private fun AjoutArticle(nom: String, question: String, description: String, type: String, user: String){
        if(selectedImageUri == null){
            println("image null")

            return
        }


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

        val apiInterface = ApiArticle.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["nom"] = RequestBody.create(MultipartBody.FORM, nom)
        if(question.isNotEmpty()){
            data["question"] = RequestBody.create(MultipartBody.FORM, question)
        }
        data["description"] = RequestBody.create(MultipartBody.FORM, description)
        data["type"] = RequestBody.create(MultipartBody.FORM, type)
        data["user"] = RequestBody.create(MultipartBody.FORM, user)

        if (profilePicture != null) {
            apiInterface.ajoutArticle(data,profilePicture).enqueue(object:
                Callback<Articles> {
                override fun onResponse(
                    call: Call<Articles>,
                    response: Response<Articles>
                ) {
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())
                        showAlertDialog()
                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<Articles>, t: Throwable) {
                    progress_bar.progress = 0
                    println("noooooooooooooooooo")
                }

            })
        }
    }

    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("L'article a été ajouté avec succes")
            .setPositiveButton("Ok") {dialog, which ->
                showSnackbar("welcome")
            }
            .show()
    }
    private fun showSnackbar(msg: String) {
        Snackbar.make(layout_root, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showdialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Ajouter une question a votre article ! ")

        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Votre question...")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            questionnn = input.text.toString()

        })
        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE){
            selectedImageUri = data?.data
            imagePicker?.setImageURI(selectedImageUri)

        }
    }
}