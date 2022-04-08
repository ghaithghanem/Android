package com.amier.Activities.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.amier.Activities.api.Api
import com.amier.Activities.api.ApiArticle
import com.amier.Activities.models.Articles
import com.amier.modernloginregister.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.text.Editable

import android.text.TextWatcher





class SearchFragment : Fragment() ,ArticleViewAdapter.OnItemClickListener{
    var filtredArticle: MutableList<Articles> = arrayListOf()
    lateinit var articlesDispo: MutableList<Articles>
    lateinit var test: ArticleViewAdapter.OnItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var searcha = ""
        test = this
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.setHasFixedSize(true)
        getNewsData { newss: List<Articles> ->
            articlesDispo = newss as MutableList<Articles>


                view.recyclerView.adapter = ArticleViewAdapter(newss, this)
        }

        val ajouterArticleButton = view.findViewById<Button>(R.id.ajouterArticle)
        val searchBar = view.findViewById<TextInputLayout>(R.id.searchBar)
        val keyword = view.findViewById<TextInputEditText>(R.id.keyword)

        ajouterArticleButton.setOnClickListener {
            val intent = Intent(activity, AjouterArticle::class.java)
            startActivity(intent)
        }
        keyword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    filtredArticle.clear()
                    articlesDispo.forEach {
                        if (it.nom!!.contains(s)) {
                            filtredArticle.add(it)
                        }
                    }
                    view.recyclerView.adapter = ArticleViewAdapter(filtredArticle, test)
                }
                if(s.isEmpty()){
                    view.recyclerView.adapter = ArticleViewAdapter(articlesDispo, test)
                }
                searcha = s.toString()
            }
        })


        return view
    }
    private fun getNewsData(callback: (List<Articles>) -> Unit) {
        val apiInterface = ApiArticle.create()

        apiInterface.GetAllArticles().enqueue(object: Callback<Articles> {
            override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Articles>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }

    override fun onItemClick(position: Int, articles: List<Articles>) {
        val intent = Intent(activity, DetailArticle::class.java)
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
    }

}