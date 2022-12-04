package com.kanabix.ui.fragment.DeliveryTab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.adapters.OrderViewAdapter
import com.kanabix.api.Constants
import com.kanabix.api.request.CreateOrderTrackingRequest
import com.kanabix.api.request.CreateOrderTrackingTransporter
import com.kanabix.api.response.ViewOrderProductDetail
import com.kanabix.databinding.FragmentPaymentViewBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.ViewPaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

@AndroidEntryPoint
class PaymentViewFragment : Fragment(),sessionExpiredListener {

    private lateinit var binding: FragmentPaymentViewBinding

    var _id = ""
    var token = ""

    // toolbar
    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    var data = ArrayList<ViewOrderProductDetail>()

    private val viewModel : ViewPaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()

        arguments?.getString("_id")?.let {
            _id = it
        }

        viewModel.paymentViewApi(token, _id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentViewBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()

        Back_Delivery.setOnClickListener {
            findNavController().popBackStack()
        }


        setAdapter(data)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponse()
    }

    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._paymentViewStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            try {
                                if (response.data?.responseCode == 200) {
                                }
                                else if(response.data?.responseCode == 440){

                                    fragmentManager?.let {
                                        LogOutDialog(this@PaymentViewFragment).show(it, "MyCustomFragment")
                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
//                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
//                            }
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


    private fun setAdapter(data: ArrayList<ViewOrderProductDetail>) {
        binding.orderDetailsRecycler.adapter = OrderViewAdapter(requireContext(), data)
    }


    fun setToolBar() {

        PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
        iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
        Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

        PreLoginTitle_TextView21.setText("Payment")

        PreLoginTitle_TextView21.visibility = View.VISIBLE
        iconsLayout1.visibility = View.VISIBLE
        Back_Delivery.visibility = View.VISIBLE
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