package com.amier.Activities.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.amier.modernloginregister.R
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        view.recycler_viewAssociation.layoutManager = LinearLayoutManager(activity)
        view.recycler_viewAssociation.setHasFixedSize(true)
        /*getNewsData { newss : List<New> ->
            view.recycler_viewAssociation.adapter = NewsViewAdapter(newss)
        }*/


        return view
    }

    /*private fun getNewsData(callback: (List<New>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getCompanyNews().enqueue(object: Callback<news> {
            override fun onResponse(call: Call<news>, response: Response<news>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.news)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<news>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }*/


}