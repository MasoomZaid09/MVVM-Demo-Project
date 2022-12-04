package com.kanabix.ui.fragment.orderFlow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanabix.R
import com.kanabix.adapters.OrderPendingAdapter
import com.kanabix.api.response.DeliveryOrderListDoc
import com.kanabix.databinding.FragmentOrderPendingBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension

import com.kanabix.interfaces.OrderManagementClick
import com.kanabix.interfaces.sessionExpiredListener

import com.kanabix.models.OrderPendingModelClass
import com.kanabix.ui.acitivity.LocationDeliveryActivity_GeneratedInjector
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.ui.fragment.DeliveryTab.OrderManagment
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.DeliveryPartnerOrderListViewModel
import com.kanabix.viewModel.SearchOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderPendingFragment() : Fragment(), OrderManagementClick, sessionExpiredListener {

    private var _binding: FragmentOrderPendingBinding? = null
    private val binding get() = _binding!!

    var data = ArrayList<DeliveryOrderListDoc>()
    var token = ""
    var again = false

    private val viewModel: DeliveryPartnerOrderListViewModel by viewModels()
    private val sharedSearchViewModel: SearchOrderViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN).toString()
        viewModel.deliveryPartnerOrderListApi(token, "PENDING", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderPendingBinding.inflate(layoutInflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponse()
        ObserveSearchResponse()
    }


    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
                viewModel._OrderListStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {

                                binding.txtNoData.visibility = View.GONE
                                data = response.data.result.docs
                                setAdapter(data)
                            }
                            else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@OrderPendingFragment).show(it, "MyCustomFragment")
                                }
                            }

                        }
                        is Resource.Error -> {

                            ProgressBar.hideProgress()
                            binding.txtNoData.visibility = View.VISIBLE
                            data.clear()
                            setAdapter(data)
                            binding.PendingRecycler.adapter?.notifyDataSetChanged()
                        }

                        is Resource.Loading -> {

                            if (!again){
                                ProgressBar.showProgress(requireContext())
                            }
                        }

                        is Resource.Empty -> {
                            ProgressBar.hideProgress()
                        }

                    }

                }
            }
    }

    private fun ObserveSearchResponse() {

        sharedSearchViewModel._SearchOrderList.observe(viewLifecycleOwner, Observer { searchText ->
            again = true
            viewModel.deliveryPartnerOrderListApi(token, "PENDING", searchText)
        })
    }


    private fun setAdapter(data: ArrayList<DeliveryOrderListDoc>) {
        binding.PendingRecycler.adapter = OrderPendingAdapter(requireContext(), data, this)
    }

    override fun orderManagementClick(orderId: String) {
        val amount = orderId
        val bundle = bundleOf("orderId" to amount)
        findNavController().navigate(R.id.orderDetailsFragment2, bundle)
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