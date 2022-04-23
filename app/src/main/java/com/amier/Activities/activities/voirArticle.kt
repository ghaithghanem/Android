package com.amier.Activities.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.amier.Activities.UserArticleListAdapter
import com.amier.Activities.api.ApiArticle
import com.amier.Activities.models.Articles
import com.amier.Activities.models.Reponse
import com.amier.modernloginregister.R
import kotlinx.android.synthetic.main.activity_detail_user_article.*
import kotlinx.android.synthetic.main.activity_detail_user_article.recycler_viewArticleList
import kotlinx.android.synthetic.main.activity_voir_article.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class voirArticle : AppCompatActivity(),ArticleViewAdapter.OnItemClickListener {
    lateinit var mSharedPref: SharedPreferences
    lateinit var idUser: String
    lateinit var test: ArticleViewAdapter.OnItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        idUser = mSharedPref.getString("_id","")!!
        setContentView(R.layout.activity_voir_article)

        myArticleRV.layoutManager = LinearLayoutManager(this)
        myArticleRV.setHasFixedSize(true)


        getAllData(idUser){ articless : List<Articles> ->
            println(articless)
            myArticleRV.adapter = ArticleViewAdapter(articless,this)

        }

    }
    private fun getAllData(idUser:String,callback: (List<Articles>) -> Unit){
        val apiInterface = ApiArticle.create()

        apiInterface.GetMyArticles(idUser).enqueue(object:
            Callback<Articles> {
            override fun onResponse(
                call: Call<Articles>,
                response: Response<Articles>
            ) {
                if(response.isSuccessful){
                    Log.i("onResponse goooood", response.body().toString())
                } else {
                    Log.i("OnResponse not good", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Articles>, t: Throwable) {
            }

        })
    }

    override fun onItemClick(position: Int, property: List<Articles>) {
        TODO("Not yet implemented")
    }
}