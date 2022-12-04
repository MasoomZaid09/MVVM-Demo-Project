package com.kanabix.ui.fragment.orderFlow

import android.Manifest
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.exobe.util.DateFormat
import com.exobe.util.DateFormat.Companion.orderDetailsDateFormat
import com.fram.farmserv.utils.Currency_from_Location
import com.google.android.gms.location.LocationServices
import com.kanabix.R
import com.kanabix.adapters.OrderViewAdapter
import com.kanabix.api.Constants.DELIVERED_MESSAGE
import com.kanabix.api.Constants.PICKED_MESSAGE
import com.kanabix.api.Constants.SHIPPED_MESSAGE
import com.kanabix.api.request.CreateOrderTrackingRequest
import com.kanabix.api.request.CreateOrderTrackingTransporter
import com.kanabix.api.response.ViewOrderProductDetail
import com.kanabix.databinding.FragmentOrderDetailsBinding
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.bottomSheetClick
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.ui.acitivity.RoleSelectScreen
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.OrderDetailsCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class OrderDetailsFragment : Fragment(), bottomSheetClick, sessionExpiredListener {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    var data = ArrayList<ViewOrderProductDetail>()

    var paymentFlag = ""
    var token = ""
    var orderid = ""

    var startDate = ""
    var endDate = ""

    val c = Calendar.getInstance()
    var yearset = 0
    var monthset = 0
    var day = 0


    var lat = 0.0
    var log = 0.0
    var originLat = 0.0
    var originLog = 0.0

    var currentStatus = ""

    // toolbar
    lateinit var iconsLayout1: LinearLayout
    lateinit var Back_Delivery: LinearLayout
    lateinit var PreLoginTitle_TextView21: TextView

    private val viewModel: OrderDetailsCommonViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
            .toString()
        arguments?.getString("orderId")?.let { orderid = it }
        paymentFlag = arguments?.getString("payment").toString()

        viewModel.viewOrderDeliveryApi(token, orderid)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        val view = binding.root

//        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.TOKEN)
//            .toString()
//        arguments?.getString("orderId")?.let { orderid = it }
//        paymentFlag = arguments?.getString("payment").toString()

        locationpermission()
        datePicker()
        findLocationClick()
        setToolBar()
        handleFlags()
        acceptOrDeclineClick()

        Back_Delivery.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun USERCURRENTLOCATION() {
        try {
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LAT)?.let {
                originLat = it.toDouble()
            }
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.LONG)?.let {
                originLog = it.toDouble()
            }

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun locationpermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            LocationClass.getCurrentLocation(requireContext())
            USERCURRENTLOCATION()
        }
    }

    private fun acceptOrDeclineClick() {

        binding.radioAccept.setOnClickListener {

            binding.thanksForOrderDeleviry.visibility = View.VISIBLE
            binding.acceptButton.setImageResource(R.drawable.ic_baseline_circle_24)
            binding.imgDotButton.setImageResource(0)
            binding.briefTheResion.visibility = View.GONE
            binding.llThanksForDeleviry.visibility = View.VISIBLE
            binding.tvBriefTheResion.visibility = View.GONE

            binding.btnConfirm.setOnClickListener {

                if (binding.tvPickDate.text.equals("")) {
                    binding.tvCouriorDate.visibility = View.VISIBLE
                    binding.tvCouriorDate.text = "*Please enter pick up date."

                } else if (binding.tvDeleviryDate.text.equals("")) {
                    binding.tvExpectedDate.visibility = View.VISIBLE
                    binding.tvCouriorDate.visibility = View.GONE
                    binding.tvExpectedDate.text = "*Please enter expected date."

                } else {
                    binding.tvExpectedDate.visibility = View.GONE
                    binding.tvCouriorDate.visibility = View.GONE
                  System.out.println("Test="+DateFormat.expectedDateAndTime(binding.tvPickDate.text.toString())+" "+DateFormat.expectedDateAndTime(binding.tvPickDate.text.toString()))
                    Log.e("Test",binding.tvPickDate.text.toString())
                    Log.e("Test1",binding.tvDeleviryDate.text.toString())

                    viewModel.acceptRejectOrderApi(token, orderid, "ACCEPTED", "",DateFormat.expectedDateAndTime(binding.tvPickDate.text.toString())
                            .toString(),
                        DateFormat.expectedDateAndTime(binding.tvDeleviryDate.text.toString())
                            .toString()
                    )
                }
            }
        }

        binding.radioDecline.setOnClickListener {
            binding.briefTheResion.visibility = View.VISIBLE
            binding.llThanksForDeleviry.visibility = View.GONE
            binding.thanksForOrderDeleviry.visibility = View.VISIBLE
            binding.acceptButton.setImageResource(0)
            binding.imgDotButton.setImageResource(R.drawable.ic_baseline_circle_24)
            binding.tvCouriorDate.visibility = View.GONE
            binding.tvExpectedDate.visibility = View.GONE

            binding.btnConfirm.setOnClickListener {

                if (binding.etBriefTheResion.text.toString() == "") {
                    binding.tvBriefTheResion.visibility = View.VISIBLE
                    binding.tvBriefTheResion.text = "*Please enter reason."
                } else {
                    binding.tvBriefTheResion.visibility = View.GONE
                    viewModel.acceptRejectOrderApi(
                        token,
                        orderid,
                        "DECLINED",
                        binding.etBriefTheResion.text.toString(),
                        null,
                        null
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObserveResponse()
        ObserveAcceptRejectResponse()
        ObserveOrderTrackingResponse()
    }

    private fun ObserveAcceptRejectResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._AcceptRejectOrderStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        ProgressBar.hideProgress()

                        if (response.data?.responseCode == 200) {

                            findNavController().popBackStack(R.id.orderManagment2, false)
                        } else if (response.data?.responseCode == 440) {

                            fragmentManager?.let {
                                LogOutDialog(this@OrderDetailsFragment).show(it, "MyCustomFragment")
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

    private fun ObserveOrderTrackingResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel._OrderTrackingFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        ProgressBar.hideProgress()

                        try {
                            if (response.data?.responseCode == 200) {

                                if (currentStatus.equals("DELIVERED")) {

                                    activity?.supportFragmentManager?.let {
                                        OtpOrderScreen(orderid, this@OrderDetailsFragment).show(
                                            it, "Bottom Sheet"
                                        )
                                    }

                                } else {
                                    viewModel.viewOrderDeliveryApi(token, orderid)
                                }


                            } else if (response.data?.responseCode == 440) {

                                fragmentManager?.let {
                                    LogOutDialog(this@OrderDetailsFragment).show(
                                        it,
                                        "MyCustomFragment"
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ObserveResponse() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._ViewOrderStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        ProgressBar.hideProgress()

                        try {
                            if (response.data?.responseCode == 200) {

                                response.data.result.apply {

                                    if (deliveryPartnerStatus.equals("PENDING")) {

                                        binding.llCancled.visibility = View.GONE
                                        binding.llProcessing.visibility = View.GONE
                                        binding.orderTrackingStatus.visibility = View.GONE
                                        binding.orderStatusLayout.visibility = View.VISIBLE

                                    } else if (deliveryPartnerStatus.equals("ACCEPTED")) {

                                        binding.btnUpdate.visibility = View.GONE
                                        binding.llCancled.visibility = View.GONE
                                        binding.llProcessing.visibility = View.VISIBLE
                                        binding.orderTrackingStatus.visibility = View.VISIBLE
                                        binding.orderStatusLayout.visibility = View.GONE


                                        binding.txtOrderStatus.text =
                                            "Order Status : ${orderStatus}"
                                        binding.datePickUpFromStore.text =
                                            DateFormat.dealsdate(pickUpDate)
                                        binding.expectedDeliveryDate.text =
                                            DateFormat.dealsdate(deliveryDateAndTime)
                                        binding.deleveredDateAndTime.text =
                                            DateFormat.deliveryDateAndTime(deliveryDateAndTime)
                                        binding.paymentStatus.text = paymentStatus

                                        binding.txtDeliveryStatus.text =
                                            "Current Status : ${trackingArray.get(trackingArray.lastIndex).statusOfTracking}"


                                        if (trackingArray.get(trackingArray.lastIndex).statusOfTracking.equals(
                                                "ORDER PLACED"
                                            )
                                        ) {
                                            setEntriesSpinner(R.array.order_status_list)

                                        } else if (trackingArray.get(trackingArray.lastIndex).statusOfTracking.equals(
                                                "PICKED"
                                            )
                                        ) {
                                            setEntriesSpinner(R.array.picked_order_status_list)

                                        } else if (trackingArray.get(trackingArray.lastIndex).statusOfTracking.equals(
                                                "SHIPPED"
                                            )
                                        ) {
                                            setEntriesSpinner(R.array.shipped_order_status_list)
                                        } else if (trackingArray.get(trackingArray.lastIndex).statusOfTracking.equals(
                                                "DELIVERED"
                                            )
                                        ) {
                                            binding.orderTrackingStatus.visibility = View.GONE
                                        }


                                        // click to update order tracking status
                                        binding.btnOrderUpdate.setOnClickListener {

                                            val request = CreateOrderTrackingRequest()
                                            val requestTranporter = CreateOrderTrackingTransporter()

                                            request.statusOfTracking =
                                                binding.typesOfOrderStatus.selectedItem.toString()
                                            request.transporter = requestTranporter
                                            request.orderId = response.data.result.id

                                            if (binding.typesOfOrderStatus.selectedItem.equals("Select")) {
                                                binding.tvDeliveryStatus.visibility = View.VISIBLE
                                                binding.tvDeliveryStatus.text =
                                                    "*Please select order status."
                                            } else {
                                                if (binding.typesOfOrderStatus.selectedItem.equals("DELIVERED")) {

                                                    currentStatus =
                                                        binding.typesOfOrderStatus.selectedItem.toString()
                                                    request.msg = DELIVERED_MESSAGE

                                                    binding.tvDeliveryStatus.visibility = View.GONE
                                                    binding.tvDeliveryStatus.text = ""

                                                } else if (binding.typesOfOrderStatus.selectedItem.equals(
                                                        "PICKED"
                                                    )
                                                ) {
                                                    request.msg = PICKED_MESSAGE
                                                    binding.tvDeliveryStatus.visibility = View.GONE
                                                    binding.tvDeliveryStatus.text = ""
                                                } else {
                                                    request.msg = SHIPPED_MESSAGE
                                                    binding.tvDeliveryStatus.visibility = View.GONE
                                                    binding.tvDeliveryStatus.text = ""
                                                }

                                                viewModel.createOrderTrackingApi(
                                                    token,
                                                    request
                                                )
                                            }
                                        }

                                    } else if (deliveryPartnerStatus.equals("DECLINED")) {
                                        binding.orderTrackingStatus.visibility = View.GONE
                                        binding.btnUpdate.visibility = View.GONE
                                        binding.llCancled.visibility = View.VISIBLE
                                        binding.llProcessing.visibility = View.GONE
                                        binding.orderStatusLayout.visibility = View.GONE

                                        binding.txtOrderCancelled.text =
                                            "Order Status : ${orderStatus}"
                                        binding.txtDeliveryStatusCancelled.text =
                                            "Delivery Status : ${deliveryPartnerStatus}"
                                        binding.deliveryDeclineDate.text =
                                            DateFormat.dealsdate(updatedAt)
                                        binding.tvReason.text = reason
                                        binding.deliveryDeclineDate.text =
                                            DateFormat.dealsdate(declinedDate)
                                    }

                                    // set products data
                                    data = response.data.result.productDetails
                                    setAdapter(data)

                                    // integrate customer details
                                    response.data.result.userId.apply {
                                        binding.txtCustomerName.text = name
                                        binding.txtCustomerNumber.text = mobileNumber
                                        binding.txtCustomerEmail.text = email
                                        binding.txtCustomerAddress.text = address
                                    }

                                    // order data
                                    binding.orderId.text =
                                        "Order ID #${response.data.result.orderId}"

                                    binding.placeOn.text =
                                        "PLACED ON ${DateFormat.deliveryDateAndTime(response.data.result.createdAt)}"

                                    // get lat long

                                    lat =
                                        response.data.result.userId.location.coordinates.get(0)
                                    log =
                                        response.data.result.userId.location.coordinates.get(1)

                                }
                            } else if (response.data?.responseCode == 440) {

                                fragmentManager?.let {
                                    LogOutDialog(this@OrderDetailsFragment).show(
                                        it,
                                        "MyCustomFragment"
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
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

    private fun datePicker() {

        // date picker

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

//
//        binding.llPickDate.setOnClickListener {
//            val datePickerDialog = this?.let { it1 ->
//                DatePickerDialog(
//                    requireContext(), R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener
//                    { view, years, monthOfYear, dayOfMonth ->
//                        year = years
//                        month = monthOfYear
//                        date = dayOfMonth
//                        val date = "$dayOfMonth ${monthOfYear + 1} $years"
//                        binding.tvPickDate.text = orderDetailsDateFormat(date)
//                    }, year, month, date
//
//                )
//            }
//            datePickerDialog!!.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
//            datePickerDialog?.show()
//        }
//
//        binding.llDeleviryDate.setOnClickListener {
//            val datePickerDialog = this?.let { it1 ->
//                DatePickerDialog(
//                    requireContext(), R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener
//                    { view, year, monthOfYear, dayOfMonth ->
//                        val date = "$dayOfMonth ${monthOfYear + 1} $year"
//
//                        binding.tvDeleviryDate.text = orderDetailsDateFormat(date)
//                    }, year, month, date
//                )
//            }
//            datePickerDialog!!.getDatePicker().setMinDate(System.() - 1000)
//            datePickerDialog?.show()
//        }


        startDate = "$date-${month + 1}-$year"

        endDate = "$date-${month}-$year"

        binding.llPickDate.setOnClickListener {

            binding.tvDeleviryDate.text = ""

            val datePickerDialog =
                DatePickerDialog(
                    requireContext(), R.style.DatePickerTheme,
                    { view, year, monthOfYear, dayOfMonth ->
                        c.set(year, monthOfYear, dayOfMonth)
                        binding.tvPickDate.text = "$dayOfMonth-${monthOfYear + 1}-$year"
                        yearset = year
                        monthset = monthOfYear
                        day = dayOfMonth

                    }, year, month, date
                )
            datePickerDialog!!.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }

        c.set(yearset, monthset, day)

        binding.llDeleviryDate.setOnClickListener {

            val datePickerDialog =
                DatePickerDialog(
                    requireContext(), R.style.DatePickerTheme,
                    { view, year, monthOfYear, dayOfMonth ->
                        binding.tvDeleviryDate.text = "$dayOfMonth-${monthOfYear + 1}-$year"

                    }, year, month, date
                )
            datePickerDialog.getDatePicker().minDate = c.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun findLocationClick() {

        binding.tvFindLocation.setOnClickListener {

            val srcAdd = "&origin=" + originLat.toString() + "," + originLog.toString()
//                        String srcAdd = "&origin=" +42.431920+ "," + -82.206580;
            //                        String srcAdd = "&origin=" +42.431920+ "," + -82.206580;
            val desAdd = "&destination=" + log.toString() + "," + lat.toString()

            val link =
                "https://www.google.com/maps/dir/?api=1&travelmode=driving$srcAdd$desAdd"

            Log.e("Url Polyline?", link)


            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            } catch (ane: ActivityNotFoundException) {
                Toast.makeText(context, "Please Install Google Maps ", Toast.LENGTH_LONG).show()
            } catch (ex: java.lang.Exception) {
                ex.message
            }
        }
    }

    private fun handleFlags() {

        if (paymentFlag.equals("PaymentReceipt")) {
            binding.orderLayout.visibility = View.GONE
            binding.reciptDetails.visibility = View.VISIBLE
            binding.orderStatusLayout.visibility = View.GONE
            binding.llProcessing.visibility = View.VISIBLE
            binding.btnUpdate.visibility = View.GONE
        } else {
            binding.orderLayout.visibility = View.VISIBLE
            binding.reciptDetails.visibility = View.GONE
            binding.orderStatusLayout.visibility = View.VISIBLE
            binding.llProcessing.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE
        }
    }

    private fun setAdapter(data: ArrayList<ViewOrderProductDetail>) {
        binding.orderDetailsRecycler.adapter = OrderViewAdapter(requireContext(), data)
    }

    fun setToolBar() {

        PreLoginTitle_TextView21 = activity?.findViewById(R.id.PreLoginTitle_TextView21)!!
        iconsLayout1 = activity?.findViewById(R.id.iconsLayout1)!!
        Back_Delivery = activity?.findViewById(R.id.Back_Delivery)!!


        if (paymentFlag.equals("PaymentReceipt")) {
            PreLoginTitle_TextView21.setText("Payment")
        } else {
            PreLoginTitle_TextView21.setText("Order Details")
        }

        PreLoginTitle_TextView21.visibility = View.VISIBLE
        iconsLayout1.visibility = View.VISIBLE
        Back_Delivery.visibility = View.VISIBLE
    }


    private fun setEntriesSpinner(entries: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            entries,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.typesOfOrderStatus.setAdapter(adapter)
    }

    override fun bottomSheetListener() {
        findNavController().popBackStack()
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