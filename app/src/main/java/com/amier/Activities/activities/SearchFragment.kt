package com.amier.Activities.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.amier.Activities.api.Api
import com.amier.Activities.models.Articles
import com.amier.Activities.models.ArticlesReponse
import com.amier.modernloginregister.R
import kotlinx.android.synthetic.main.fragment_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.setHasFixedSize(true)
        getNewsData { newss : List<Articles> ->
            view.recyclerView.adapter = ArticleViewAdapter(newss)
        }



        return view
    }
    private fun getNewsData(callback: (List<Articles>) -> Unit) {
        val apiInterface = Api.create()
        apiInterface.GetAllArticles().enqueue(object: Callback<ArticlesReponse> {
            override fun onResponse(call: Call<ArticlesReponse>, response: Response<ArticlesReponse>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<ArticlesReponse>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }

}