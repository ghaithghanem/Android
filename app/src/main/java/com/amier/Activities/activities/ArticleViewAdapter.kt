package com.amier.Activities.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.amier.Activities.models.Articles
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.article_item.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ArticleViewAdapter    (private val listArticle : List<Articles>,private val listener: OnItemClickListener):
    RecyclerView.Adapter<ArticleViewAdapter.MyViewHolder>(){
    val DayInMilliSec = 86400000

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {



        @SuppressLint("ResourceAsColor")
        fun bind(property: Articles){
            itemView.ArticleName.text = property.nom
            itemView.ArticleDescription.text = property.description
            itemView.Creation.text = getDateTime(property.dateCreation!!)
            itemView.textView9.text = property.type

//            if(property.type.equals("Lost")){
//                itemView.article.setBackgroundColor(Color.RED)
//            }else{
//                itemView.article.setBackgroundColor(Color.GREEN)
//            }
            Glide.with(itemView).load(property.photo).into(itemView.ArticleImage)
        }
        init {
            itemView.setOnClickListener (this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position,listArticle)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int,property: List<Articles>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false))
    }
    override fun getItemCount(): Int {
        return listArticle.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(listArticle.get(position))
    }


    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/YYYY")
            val netDate = Date(s.toLong()  ).addDays(1)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun Date.addDays(numberOfDaysToAdd: Int): Date{
        return Date(this.time + numberOfDaysToAdd * DayInMilliSec)
    }
}