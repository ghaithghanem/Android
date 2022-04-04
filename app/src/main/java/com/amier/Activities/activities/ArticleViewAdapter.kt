package com.amier.Activities.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amier.Activities.models.Articles
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.article_item.view.*

class ArticleViewAdapter(private val companyList : List<Articles>): RecyclerView.Adapter<ArticleViewAdapter.MyViewHolder>(){


    class MyViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bind(property: Articles){
            itemView.ArticleName.text = property.nom
            itemView.ArticleDescription.text = property.description
            Glide.with(itemView).load(property.photo).into(itemView.ArticleImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false))
    }
    override fun getItemCount(): Int {
        return companyList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(companyList.get(position))
    }


}