package com.kanabix.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.exobe.Utils.CommonFunctions
import com.kanabix.R
import com.kanabix.adapters.MyOrderDescriptionAdapter
import com.kanabix.adapters.orderDescriptionAdapter
import com.kanabix.api.response.ViewOrderProductDetail
import com.kanabix.api.response.ViewOrderTrackingArray
import com.kanabix.databinding.FragmentOrderSummaryBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.ProductClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.models.orderDescriptionModelClass
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.ViewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderSummaryFragment : Fragment(), ProductClick ,sessionExpiredListener {

    private lateinit var binding: FragmentOrderSummaryBinding


    lateinit var adapter: orderDescriptionAdapter
    lateinit var orderAdapter: MyOrderDescriptionAdapter

    val itemList: ArrayList<orderDescriptionModelClass> = ArrayList()
    var trackingArray: ArrayList<ViewOrderTrackingArray> = ArrayList()
    var productData: ArrayList<ViewOrderProductDetail> = ArrayList()

    // toolbar
    lateinit var iconsLayout: LinearLayout
    lateinit var profile_ll: LinearLayout
    lateinit var Back: LinearLayout
    lateinit var PreLoginTitle_TextView2: TextView

    lateinit var greyBell_ImageView: ImageView
    lateinit var greenBell_ImageView: ImageView
    lateinit var greyCart: ImageView
    lateinit var greenCart: ImageView
    lateinit var greenHeart: ImageView
    lateinit var greyHeart: ImageView

    var orderId = ""
    var token = ""
    var flag = ""

    private val viewModel: ViewOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderSummaryBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        arguments?.getString("orderId")?.let {
            orderId = it
        }

        arguments?.getString("flag")?.let {
            flag = it
        }

        setToolBar()


        Back.setOnClickListener {

            if (flag.equals("OrderManagement")) {
                findNavController().popBackStack()
            } else {
                findNavController().navigate(R.id.action_orderDescription_to_notification)
            }

        }


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
            .toString()
        viewModel.viewOrderApi(token, orderId)

        val data1 = orderDescriptionModelClass(
            "Order Confirmed",
            "",
            R.drawable.ic_outline_circle_24,
            false, ""
        )
        val data2 = orderDescriptionModelClass(
            "Processing ",
            "Your Order Still reeds to be processed by or it to you.",
            R.drawable.ic_outline_circle_24,
            false, ""
        )

        val data3 = orderDescriptionModelClass(
            "Order processed by the store status",
            "Your order is confirmed by the store.",
            R.drawable.ic_outline_circle_24,
            false, ""
        )
        val data4 = orderDescriptionModelClass(
            "Shipped",
            "Delivery on your way.",
            R.drawable.ic_outline_circle_24,
            false, ""
        )
        val data5 = orderDescriptionModelClass(
            "Delivered at your point",
            "Your item has been delivered.",
            R.drawable.ic_outline_circle_24,
            false, ""
        )

        itemList.clear()
        itemList.add(data1)
        itemList.add(data2)
        itemList.add(data3)
        itemList.add(data4)
        itemList.add(data5)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (flag.equals("OrderManagement")) {
                        findNavController().popBackStack()
                    } else {
                        findNavController().navigate(R.id.action_orderDescription_to_notification)
                    }

                }
            })

        ObserveResponse()
    }

    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel._ViewOrderStateFlow.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            ProgressBar.hideProgress()

                            if (response.data?.responseCode == 200) {
                                binding.orderId.text = "Order ID ${response.data.result.orderId}"
                                binding.bagPrice.text =
                                    "${CommonFunctions.currencyFormatter(response.data.result.actualPrice.toDouble())}"
                                binding.txtTotalPrice.text =
                                    "${CommonFunctions.currencyFormatter(response.data.result.orderPrice.toDouble())}"

                                productData = response.data.result.productDetails

                                // data added on address
                                response.data.result.shippingFixedAddress.apply {
                                    binding.txtCustomerName.text = name
                                    binding.txtCustomerNumber.text = "+$countryCode-${mobileNumber}"
                                    binding.txtCustomerPincode.text = "Pincode - ${zipCode}"
                                    binding.txtCustomerAddress.text = address
                                }

                                // add order tracking
                                if (response.data.result.trackingArray.size == 0) {

                                    binding.txtOrderTracking.visibility = View.GONE
                                    binding.trackingRecyclerView.visibility = View.GONE

                                } else {
                                    binding.txtOrderTracking.visibility = View.VISIBLE
                                    binding.trackingRecyclerView.visibility = View.VISIBLE

                                    for (i in 0 until response.data.result.trackingArray.size) {
                                        var data = response.data.result.trackingArray[i]
                                        if (data.statusOfTracking.equals("ORDER PLACED")) {
                                            itemList.get(0).statusFlag = true
                                            itemList.get(0).createdAt = data.createdAt
                                            itemList.get(0).statusName = "ORDER PLACED"
                                            itemList.get(1).statusName = "ORDER PLACED"
                                            itemList.get(1).statusFlag = true
                                        } else if (data.statusOfTracking.equals("PICKED")) {
                                            itemList.get(2).statusName = "PICKED"
                                            itemList.get(2).statusFlag = true

                                        } else if (data.statusOfTracking.equals("SHIPPED")) {
                                            itemList.get(3).statusName = "SHIPPED"
                                            itemList.get(3).statusFlag = true

                                        } else if (data.statusOfTracking.equals("DELIVERED")) {
                                            itemList.get(4).statusName = "DELIVERED"
                                            itemList.get(4).statusFlag = true

                                        }
                                    }
                                    setupTrackingAdapter(itemList)
                                }


                                binding.shippingPrice.text =
                                    CommonFunctions.currencyFormatter(response.data.result.deliveryFee.toDouble())
                                setProductsAdapter(productData)
                            }else if(response.data?.responseCode == 440){

                                fragmentManager?.let {
                                    LogOutDialog(this@OrderSummaryFragment).show(it, "MyCustomFragment")
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

    private fun setupTrackingAdapter(data: ArrayList<orderDescriptionModelClass>) {
        binding.trackingRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = orderDescriptionAdapter(activity as Context, data)
        binding.trackingRecyclerView.adapter = adapter
    }

    fun setProductsAdapter(data: ArrayList<ViewOrderProductDetail>) {

        orderAdapter = MyOrderDescriptionAdapter(requireContext(), data, this)
        var layoutManager = LinearLayoutManager(requireContext())
        binding.storeRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.storeRecyclerView.getContext(),
            layoutManager.getOrientation(),
        )
        binding.storeRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.storeRecyclerView.adapter = orderAdapter
    }

    fun setToolBar() {

        PreLoginTitle_TextView2 = activity?.findViewById(R.id.PreLoginTitle_TextView2)!!
        profile_ll = activity?.findViewById(R.id.profile_ll)!!
        iconsLayout = activity?.findViewById(R.id.iconsLayout)!!
        Back = activity?.findViewById(R.id.imageView_back)!!

        val Add_address = activity?.findViewById<Button>(R.id.Add_address)!!
        Add_address.visibility = View.GONE

        PreLoginTitle_TextView2.setText("My Orders")
        PreLoginTitle_TextView2.visibility = View.VISIBLE
        iconsLayout.visibility = View.VISIBLE
        profile_ll.visibility = View.GONE
        Back.visibility = View.VISIBLE

        greenCart = activity?.findViewById(R.id.greenCart)!!
        greyCart = activity?.findViewById(R.id.greyCart)!!
        greenBell_ImageView = activity?.findViewById(R.id.greenBell_ImageView)!!
        greyBell_ImageView = activity?.findViewById(R.id.greyBell_ImageView)!!
        greenHeart = activity?.findViewById(R.id.greenHeart)!!
        greyHeart = activity?.findViewById(R.id.greyHeart)!!

        greenCart.visibility = View.GONE
        greyCart.visibility = View.VISIBLE
        greenBell_ImageView.visibility = View.GONE
        greyBell_ImageView.visibility = View.VISIBLE
        greyHeart.visibility = View.VISIBLE
        greenHeart.visibility = View.GONE
    }

    override fun sendProductId(productId: String) {
        val amount = productId
        val bundle = bundleOf("productId" to amount)
        findNavController().navigate(R.id.viewProduct, bundle)
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










