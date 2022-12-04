package com.kanabix.ui.fragment.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.kanabix.R
import com.kanabix.databinding.FragmentRerferIdBinding

import com.kanabix.ui.fragment.legals.FaqFragment
import com.kanabix.ui.fragment.legals.TermsFragment
import com.kanabix.utils.SavedPrefManager


class RerferId : Fragment() {

    private var _binding: FragmentRerferIdBinding? = null
    private val binding get() = _binding!!

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    var role = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRerferIdBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE)?.let {
            role = it
        }



        if (role.equals("Customer")){

            setToolBar()

            Back.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.txtTerms.setOnClickListener {

                findNavController().navigate(R.id.action_rerferId_to_termsFragment)
            }

            binding.txtFaqs.setOnClickListener {
                findNavController().navigate(R.id.action_rerferId_to_faqFragment)
            }
        }else{

            setToolBarDelivery()

            Back_Delivery.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.txtTerms.setOnClickListener {

                findNavController().navigate(R.id.action_rerferId2_to_termsFragment2)
            }

            binding.txtFaqs.setOnClickListener {
                findNavController().navigate(R.id.action_rerferId2_to_faqFragment2)
            }
        }

        binding.btnRefer.setOnClickListener{
            val i = Intent(Intent.ACTION_SEND)
            i.setType("text/plain")
            val shareBody = "https://play.google.com/store/apps/details?id=com.flipkart.android"
            val shareSubject = "Kanabix Link :"
            i.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            i.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(i, "Sharing using"))
        }


        return view
    }

    fun setToolBar() {

        try{
            PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
            profile_ll = activity?.findViewById(R.id.profile_ll)!!
            iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
            Back = activity?.findViewById(R.id.imageView_back)!!
            val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
            Add_address.visibility = View.GONE

            PreLoginTitle_TextView2.setText("Refer and Earn")
            PreLoginTitle_TextView2.visibility = View.VISIBLE
            iconsLayout.visibility=View.GONE
            profile_ll.visibility=View.GONE
            Back.visibility = View.VISIBLE
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    fun setToolBarDelivery() {

        try{
            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.setText("Refer and Earn")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility=View.GONE
            Back_Delivery.visibility = View.VISIBLE
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
}