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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailArticle : AppCompatActivity()  {
    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)




        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val name = intent.getStringExtra("nom")
        //val addresse = intent.getStringExtra("addresse")
        val _id = intent.getStringExtra("_id")
        val userArticleid = intent.getStringExtra("userDetail")
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
            showdialog(questionTitle!!)
        }

    }
    fun showdialog(question:String){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Repondez à la question ! ")

        builder.setMessage(question)
        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Votre réponse...")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var m_Text = input.text.toString()
            print(m_Text);
        })
        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

}