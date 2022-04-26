package com.amier.Activities.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.amier.Activities.UserArticleListAdapter
import com.amier.Activities.api.ApiArticle
import com.amier.Activities.api.ApiQuestion
import com.amier.Activities.models.Articles
import com.amier.Activities.models.Question
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_user_article.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserArticle : AppCompatActivity(), UserArticleListAdapter.OnItemClickListener {
    lateinit var mSharedPref: SharedPreferences
    lateinit var userArticleid: String
    lateinit var articleUser: MutableList<Articles>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_article)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

         userArticleid = intent.getStringExtra("id")
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
        recycler_viewArticleList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        recycler_viewArticleList.setHasFixedSize(true)

        getAllData{ articless : MutableList<Articles> ->
            if(articless.isEmpty()){
                Articles.text = "Pas d'articles à afficher "
            }
            recycler_viewArticleList.adapter = UserArticleListAdapter(articless,this)

        }



    }
    private fun getAllData(callback: (MutableList<Articles>) -> Unit){
        val apiInterface = ApiArticle.create()
        val id = userArticleid
        apiInterface.GetArticlesByUser(id).enqueue(object:
            Callback<Articles> {
            override fun onResponse(
                call: Call<Articles>,
                response: Response<Articles>
            ) {
                if(response.isSuccessful){
                    Log.i("onResponse goooood", response.body().toString())
                    return callback(response.body()!!.articles!!)
                } else {
                    Log.i("OnResponse not good", response.body().toString())
                }
            }
            override fun onFailure(call: Call<Articles>, t: Throwable) {
            }
        })
    }

    override fun onItemClick(position: Int, articles: List<Articles>) {
        val intent = Intent(this@DetailUserArticle, DetailArticle::class.java)
        intent.putExtra("nom",articles[position].nom)
        intent.putExtra("addresse",articles[position].addresse)
        intent.putExtra("_id",articles[position]._id)
        intent.putExtra("description",articles[position].description)
        intent.putExtra("type",articles[position].type)
        intent.putExtra("photo",articles[position].photo)
        intent.putExtra("userArticleNom", articles[position].user?.nom)
        intent.putExtra("userArticlePrenom", articles[position].user?.prenom)
        intent.putExtra("userArticlePhoto", articles[position].user?.photoProfil)
        intent.putExtra("userArticleEmail", articles[position].user?.email)
        intent.putExtra("userDetail", articles[position].user?._id)
        intent.putExtra("question", articles[position].question?._id)
        intent.putExtra("questionTitle", articles[position].question?.titre)
        startActivity(intent)
        this.finish()
    }
}