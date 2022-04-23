package com.amier.Activities.activities

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
import android.widget.*
import com.amier.Activities.api.ApiArticle
import com.amier.Activities.api.ApiQuestion
import com.amier.Activities.models.Articles
import com.amier.Activities.models.Reponse
import com.amier.Activities.models.User
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArticle : AppCompatActivity()  {
    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)




        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val name = intent.getStringExtra("nom")
        //val addresse = intent.getStringExtra("addresse")
        val idd = intent.getStringExtra("_id")
        val userArticleid = intent.getStringExtra("userDetail")
        val questionID = intent.getStringExtra("question")
        val questionTitle = intent.getStringExtra("questionTitle")
        val userArticleNom = intent.getStringExtra("userArticleNom")
        val userArticlePrenom = intent.getStringExtra("userArticlePrenom")
        val userArticlePhoto = intent.getStringExtra("userArticlePhoto")
        val userArticleEmail = intent.getStringExtra("userArticleEmail")
        val question = intent.getStringExtra("question")
        val namee = findViewById<TextView>(R.id.textView4)
        namee.text = name
        val photo = findViewById<ImageView>(R.id.imageView)
        val warning = findViewById<TextView>(R.id.textView6)
        Glide.with(applicationContext).load(Uri.parse(intent.getStringExtra("photo"))).into(photo)

        val description = findViewById<TextView>(R.id.editTextTextMultiLine3)
        description.text = intent.getStringExtra("description")

        val type = findViewById<TextView>(R.id.textView5)
        type.text = intent.getStringExtra("type")

        val repondreButton = findViewById<Button>(R.id.repondre)
        val voirProfilButton = findViewById<Button>(R.id.profil)
        if(question == null){

            repondreButton.visibility = View.GONE
        }
        if(userArticleid==mSharedPref.getString("_id",null)){
            voirProfilButton.visibility = View.GONE
            repondreButton.visibility = View.GONE
            warning.text = "C'est votre article"
        }


        voirProfilButton.setOnClickListener {

            val intent = Intent(applicationContext, DetailUserArticle::class.java)
            intent.putExtra("id",userArticleid)
            intent.putExtra("nom",userArticleNom)
            intent.putExtra("prenom",userArticlePrenom)
            intent.putExtra("email",userArticleEmail)
            intent.putExtra("photo",userArticlePhoto)
            startActivity(intent)
        }


        repondreButton.setOnClickListener {
            showdialog(mSharedPref.getString("_id","")!!,questionTitle!!,questionID!!)
        }
        

    }
    fun showdialog(idd:String,question:String,questionID:String){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Repondez à la question ! ")

        builder.setMessage(question)
        val input = EditText(this)
        input.setHint("Votre réponse...")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            val reponse= Reponse()
            reponse.description = input.text.toString()
            reponse.userr = idd
            val apiInterface = ApiQuestion.create()
            apiInterface.postReponse(questionID,reponse).enqueue(object:
                Callback<Reponse> {
                override fun onResponse(
                    call: Call<Reponse>,
                    response: Response<Reponse>
                ) {
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())
                    } else if (response.code() == 400){
                        Toast.makeText(applicationContext, "Vous avez déjà repondu a la question !", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Reponse>, t: Throwable) {
                }
            })
        })
        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

}