package com.kanabix.ui.fragment.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.kanabix.R
import com.kanabix.databinding.FragmentSettingsBinding
import com.kanabix.ui.fragment.location.ChooseDeliveryAddressFragmentDirections


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // toolbar
    lateinit var iconsLayout1 : LinearLayout
    lateinit var Back_Delivery : LinearLayout
    lateinit var PreLoginTitle_TextView21 : TextView

    var amount = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        Back_Delivery.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnEditBankDetails.setOnClickListener {

            amount = "View"
            val bundle = bundleOf("settings" to amount)
            findNavController().navigate(R.id.viewBankDetails2, bundle)

        }

        binding.btnChangePassword.setOnClickListener {

            findNavController().navigate(R.id.action_settingsFragment2_to_changepassword2)
        }

        return view
    }


    fun setToolBar() {

        try{

            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.visibility = View.VISIBLE
            PreLoginTitle_TextView21.text = "Settings"
            iconsLayout1.visibility=View.GONE
            Back_Delivery.visibility = View.VISIBLE

        }catch (e:Exception){
            e.printStackTrace()
        }



    }
}