package com.kanabix.ui.fragment.DeliveryTab

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.kanabix.R
import com.kanabix.adapters.viewPagerAdapter
import com.kanabix.api.Constants
import com.kanabix.databinding.FragmentOrderManagmentBinding
import com.kanabix.ui.fragment.orderFlow.OrderCancelledFragment
import com.kanabix.ui.fragment.orderFlow.OrderPendingFragment
import com.kanabix.ui.fragment.orderFlow.OrderProcessingFragment
import com.kanabix.viewModel.SearchOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderManagment : Fragment() {

    private var _binding: FragmentOrderManagmentBinding? = null
    private val binding get() = _binding!!

    // toolbar
    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    lateinit var adapter : viewPagerAdapter

    var search = ""

    // get shared view model for search orders
    private val sharedSearchViewModel : SearchOrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderManagmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        searchHandle()
        setToolBar()
        setupTabs()

        return view
    }

    fun searchHandle() {

        binding.DFsearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                searchFlag = true
                sharedSearchViewModel.saveSearchText(binding.DFsearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        var job: Job? = null
        binding.DFsearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                editable?.let {
                    if (editable.toString().isEmpty()) {
                        sharedSearchViewModel.saveSearchText("")
                    }
                }
            }
        }
    }

    private fun setupTabs() {
        adapter = viewPagerAdapter(this.childFragmentManager)
        adapter.getFragment(OrderPendingFragment(), "PENDING")
        adapter.getFragment(OrderProcessingFragment(), "PROCESSING")
        adapter.getFragment(OrderCancelledFragment(), "CANCELLED")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }


    fun setToolBar() {

        try {
            PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
            iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
            Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!

            PreLoginTitle_TextView21.setText("Order")
            PreLoginTitle_TextView21.visibility = View.VISIBLE
            iconsLayout1.visibility = View.VISIBLE
            Back_Delivery.visibility = View.GONE

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
