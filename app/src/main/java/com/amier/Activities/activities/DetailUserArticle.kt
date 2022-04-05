package com.amier.Activities.activities

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide

class DetailUserArticle : AppCompatActivity() {
    lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_article)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val userArticleid = intent.getStringExtra("id")
        val userArticleNom = intent.getStringExtra("nom")
        val userArticlePrenom = intent.getStringExtra("prenom")
        val userArticlePhoto = intent.getStringExtra("photo")
        val userArticleEmail = intent.getStringExtra("email")


        val photo = findViewById<ImageView>(R.id.imageUser)
        val nom = findViewById<TextView>(R.id.userArticleNom)
        val prenom = findViewById<TextView>(R.id.prenomUser)
        nom.text = userArticleNom
        prenom.text = userArticlePrenom
        val repondreButton = findViewById<Button>(R.id.voirProfilB)
        Glide.with(applicationContext).load(Uri.parse(userArticlePhoto)).into(photo)


        repondreButton.setOnClickListener {
            Log.i("","")
        }
    }
}