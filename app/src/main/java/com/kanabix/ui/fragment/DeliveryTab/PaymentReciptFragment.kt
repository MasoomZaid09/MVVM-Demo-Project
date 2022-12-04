package com.kanabix.ui.fragment.DeliveryTab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanabix.R
import com.kanabix.adapters.PaymentReciptAdapter
import com.kanabix.api.response.PaymentListResult
import com.kanabix.api.response.response
import com.kanabix.databinding.FragmentPaymentReciptBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.PaymentManagement
import com.kanabix.interfaces.PaymentManagementListener
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.models.PaymentReciptModelClass
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.fragment.payments.PaymentDetailsFragmentDirections
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_DefaultViewModelFactories_FragmentEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentReciptFragment : Fragment() , PaymentManagementListener ,sessionExpiredListener {

    private var _binding: FragmentPaymentReciptBinding? = null
    private val binding get() = _binding!!

    var data = ArrayList<PaymentListResult>()

    // toolbar
    lateinit var iconsLayout1 : LinearLayout
    lateinit var Back_Delivery : LinearLayout
    lateinit var PreLoginTitle_TextView21 : TextView

    var amount = ""
    var token = ""

    private val viewModel : PaymentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.TOKEN).toString()
        viewModel.paymentListApi(token)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentPaymentReciptBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        setToolBar()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponse()
    }


    private fun ObserveResponse(){

        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._paymentListStateFlow.collectLatest {  response ->

                    when(response){

                        is Resource.Success ->{

                            ProgressBar.hideProgress()
                            if (response.data?.responseCode == 200){

                                binding.txtNoData.visibility = View.GONE
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@PaymentReciptFragment).show(it, "MyCustomFragment")
                                }
                            }
                        }

                        is Resource.Error ->{

                            ProgressBar.hideProgress()
                            data.clear()
                            setupAdapter(data)
                            binding.txtNoData.visibility = View.VISIBLE
                            response.data?.responseMessage?.let {
                                androidExtension.alertBox(it, requireContext())
                            }
                        }

                        is Resource.Loading ->{
                            ProgressBar.showProgress(requireContext())
                        }

                        is Resource.Empty ->{
                            ProgressBar.hideProgress()
                        }
                    }

                }
            }
        }
    }


    override fun paymentListener(_id: String) {

        val bundle = bundleOf("_id" to _id)
        findNavController().navigate(R.id.action_paymentReciptFragment_to_paymentViewFragment, bundle)

    }

    private fun setupAdapter(data: ArrayList<PaymentListResult>) {
        binding.PaymentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.PaymentRecycler.adapter = PaymentReciptAdapter(requireContext(), data, this)
    }

    fun setToolBar() {

        try {
            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.setText("Payment")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility=View.VISIBLE
            Back_Delivery.visibility = View.GONE

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