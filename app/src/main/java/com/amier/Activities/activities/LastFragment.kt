package com.amier.Activities.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import com.amier.modernloginregister.R


class LastFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_last, container, false)

        val menu = view.findViewById<ImageView>(R.id.menu_dropDown)
        menu.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(requireContext(), menu)

            popupMenu.menuInflater.inflate(R.menu.last_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                   /* R.id.Securite ->

                    R.id.ModifierProfile ->
                        //categorybtn.text = "high tech"
                    R.id.Theme ->
                        //categorybtn.text = "beauty"
                    R.id.Deconnexion ->
                        //categorybtn.text = "baby"
                    R.id.SupprimerProfil ->
                        //categorybtn.text = "Jewellery"
                    println("dds")*/
                }
                true
            })
            popupMenu.show()
        }





        return view
    }


    private fun navigateToCompanyProfile() {
        val intent = Intent(requireContext(), LastFragment::class.java)
        startActivity(intent)
    }
}