package com.amier.Activities.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

import com.amier.Activities.storage.SharedPrefManager
import com.amier.modernloginregister.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_last.view.*


class LastFragment : Fragment() {
    lateinit var mSharedPref: SharedPreferences
    lateinit var pic: ImageView
    lateinit var usernameProfile: TextView
    lateinit var emailnameProfile: TextView
    lateinit var phone: TextView
    private lateinit var B1: Button
    private lateinit var edit: Button
    private lateinit var deco: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_last, container, false)
        mSharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        pic = view.findViewById<ImageView?>(R.id.imageUser)
        val picStr: String = mSharedPref.getString("photoProfil", "user.email").toString()
        Glide.with(this).load(Uri.parse(picStr)).into(pic)


        emailnameProfile = view.findViewById<TextView?>(R.id.nameUser)
        val emailStr: String = mSharedPref.getString("nom", "user.email").toString()
        emailnameProfile.text = emailStr


        usernameProfile = view.findViewById<TextView?>(R.id.emailUser)
        val nameStr: String = mSharedPref.getString("email", "user.email").toString()
        usernameProfile.text = nameStr


        deco = view.findViewById<ImageButton?>(R.id.deco)
        deco.setOnClickListener {
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle("Logout")
            builder.setMessage("logout")
            builder.setPositiveButton("Yes"){ dialogInterface, which ->
                requireActivity().getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE).edit().clear().apply()
                //finish()
                val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("No"){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()
        }

        edit = view.findViewById<Button?>(R.id.edit)
        edit.setOnClickListener {
            val intent = Intent(requireContext(), UpdateActivity::class.java)
            startActivity(intent)
        }



        phone = view.findViewById<TextView?>(R.id.phone)
        val phoneStr: String = mSharedPref.getString("numt", "user.email").toString()
        phone.text = phoneStr
/*val menu = view.findViewById<ImageView>(R.id.menu_dropDown)
menu.setOnClickListener {
    val popupMenu: PopupMenu = PopupMenu(requireContext(), menu)

    popupMenu.menuInflater.inflate(R.menu.last_menu, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
        when (item.itemId) {
            //R.id.ModifierProfile ->supportFragmentManager.beginTransaction().replace(R.layout.activity_update, UpdateActivity()).commit()
           /* R.id.Securite ->

            R.id.ModifierProfile ->
                //categorybtn.text = "high tech"
            R.id.Theme ->
                //categorybtn.text = "beauty"
            R.id.Deconnexion ->
                categorybtn.text = "baby"
            R.id.SupprimerProfil ->
                //categorybtn.text = "Jewellery"
            println("dds")*/
        }
        true
    })
    popupMenu.show()
}*/





return view
}


private fun navigateToCompanyProfile() {
val intent = Intent(requireContext(), LastFragment::class.java)
startActivity(intent)
}
}