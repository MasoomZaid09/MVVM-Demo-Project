package com.kanabix.ui.fragment.legals

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kanabix.R
import com.kanabix.databinding.FragmentRerferIdBinding
import com.kanabix.databinding.FragmentTermsBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.*
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.SharedStaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsFragment : Fragment() ,sessionExpiredListener {

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    private val sharedStaticViewModel : SharedStaticContentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTermsBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        if (SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.ROLE)
                .equals("Customer")
        ) {
            setToolBar()

            Back.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            setToolBarDelivery()

            Back_Delivery.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        sharedStaticViewModel.termsListApi()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponse()
    }

    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                sharedStaticViewModel._termsListStateFlow.collectLatest { response ->

                    when(response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200){

                                binding.txtTerms.text = response.data.result.description
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@TermsFragment).show(it, "MyCustomFragment")
                                }
                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            ProgressBar.showProgress(requireContext())
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }

                    }

                }
            }
        }
    }

    fun setToolBar() {

        try{
            PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
            profile_ll = activity?.findViewById(R.id.profile_ll)!!
            iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
            Back = activity?.findViewById(R.id.imageView_back)!!

            val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
            Add_address.visibility = View.GONE

            PreLoginTitle_TextView2.setText("Terms & Conditions")
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

            PreLoginTitle_TextView21.setText("Terms & Conditions")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility = View.GONE
            Back_Delivery.visibility = View.VISIBLE

        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    override fun sessionExpiredClick() {

        SavedPrefManager.savePreferenceBoolean(
            requireContext(),
            SavedPrefManager.loggedIn,
            false
        )

        Intent(requireContext(), RoleSelectScreen::class.java).also {
            startActivity(it)
            requireActivity().finishAffinity()
        }
    }

}