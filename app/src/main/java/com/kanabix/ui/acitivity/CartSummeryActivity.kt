package com.kanabix.ui.acitivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exobe.Utils.CommonFunctions
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.google.gson.JsonObject
import com.kanabix.R
import com.kanabix.adapters.CartSummeryAdapter
import com.kanabix.api.request.FinalCreateOrderRequest
import com.kanabix.api.request.OrderCreateRequest
import com.kanabix.api.request.checkOutRequest
import com.kanabix.api.response.CartListResponse
import com.kanabix.api.response.ViewAddressResponse
import com.kanabix.api.response.response
import com.kanabix.databinding.ActivityCartSummeryBinding
import com.kanabix.dialogs.CartDeleteDialog
import com.kanabix.dialogs.LogOutDialog
import com.kanabix.extensions.androidExtension
import com.kanabix.interfaces.paymentDialogClickListener
import com.kanabix.interfaces.sessionExpiredListener
import com.kanabix.models.CartSummeryModelClass
import com.kanabix.utils.ProgressBar
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import com.kanabix.viewModel.CartViewModel
import com.kanabix.viewModel.PaymentCartScreenViewModel
import com.kanabix.viewModel.SharedLoginViewModel
import com.razorpay.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject

@AndroidEntryPoint
class CartSummeryActivity : AppCompatActivity(), paymentDialogClickListener,
    PaymentResultWithDataListener ,sessionExpiredListener{

    private lateinit var binding: ActivityCartSummeryBinding

    private val viewModel: PaymentCartScreenViewModel by viewModels()
    private val sharedLoginViewModel: SharedLoginViewModel by viewModels()

    var token = ""
    var addressId = ""
    var BAG_AMOUNT = 0.0
    var FINAL_AMOUNT = 0.0
    var PER_PRODUCT_DELIVERY_FEE = 20

    var finalCreateOrderRequest = FinalCreateOrderRequest()

    var data = ArrayList<CartSummeryModelClass>()
    var ViewAddressResult = ViewAddressResponse.ViewAddressResult()
    var flag = ""
    var checkOutId = ""
    val jsonObject = checkOutRequest()
    var orderIds : ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartSummeryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Checkout.preload(applicationContext)

        token = SavedPrefManager.getStringPreferences(this@CartSummeryActivity, SavedPrefManager.TOKEN).toString()

        intent?.getStringExtra("_id")?.let { addressId = it }

        ObserveCartListResponse()
        ObserveViewAddressResponce()
        ObserveOrderCreateResponce()
        ObserverCheckQuantityResponse()
        ObserveCheckOutResponce()

        binding.btnBack.setOnClickListener { onBackPressed() }

        lifecycleScope.launch(Dispatchers.IO) {
            async {
                viewModel.cartListApi(token)
                viewModel.viewAddressApi(token, addressId)
            }.await()
        }

        binding.btnCartContinue.setOnClickListener {
            viewModel.orderCreateApi(token, finalCreateOrderRequest)
        }
    }


    fun startPayment(checkOutId: String) {

        val co = Checkout()

        try {

            val options = JSONObject()
            options.put("name", ViewAddressResult.name)

            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
//            options.put("theme.color", )
            options.put("currency", "INR")
            options.put("order_id", checkOutId)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            co.open(this@CartSummeryActivity, options)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun ObserveCheckOutResponce() {

        viewModel._checkOutStateFlow.observe(this, Observer  { response ->

            when (response) {

                is Resource.Success -> {
                    ProgressBar.hideProgress()
                    if (response.data?.responseCode == 200) {

                        checkOutId = response.data.result.transactionDetails.id

                        if (!checkOutId.equals("")) {
                            startPayment(checkOutId)
                        }

                    }else if(response.data?.responseCode == 440){

                        supportFragmentManager.let {
                            LogOutDialog(this).show(it, "MyCustomFragment")
                        }
                    }
                }

                is Resource.Error -> {

                    ProgressBar.hideProgress()
                    response.message?.let { message ->
                        androidExtension.alertBox(message, this@CartSummeryActivity)
                    }
                }

                is Resource.Loading -> {
                    ProgressBar.showProgress(this@CartSummeryActivity)
                }

                is Resource.Empty -> {
                    ProgressBar.hideProgress()
                }

            }

        })
    }

    private fun ObserveVerifyPaymentResponse() {

        viewModel._verifyPaymentStateFlow.observe(this, Observer { response ->

            when (response) {

                is Resource.Success -> {
                    ProgressBar.hideProgress()
                    if (response.data?.responseCode == 200) {

                        try {
                            try {
                                androidExtension.showPaymentDialog(
                                    "Total : ${binding.summeryFinalPrice.text}",
                                    this@CartSummeryActivity,
                                    this@CartSummeryActivity,
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else if(response.data?.responseCode == 440){

                        supportFragmentManager.let {
                            LogOutDialog(this).show(it, "MyCustomFragment")
                        }
                    }
                }

                is Resource.Error -> {

                    ProgressBar.hideProgress()
                    response.message?.let { message ->
                        androidExtension.alertBox(message, this@CartSummeryActivity)
                    }
                }

                is Resource.Loading -> {
                    ProgressBar.showProgress(this@CartSummeryActivity)
                }

                is Resource.Empty -> {
                    ProgressBar.hideProgress()
                }

            }

        })

    }

    private fun ObserveOrderCreateResponce() {

        viewModel._orderCreateStateFlow.observe(this,Observer { response ->

            when (response) {

                is Resource.Success -> {
                    ProgressBar.hideProgress()
                    if (response.data?.responseCode == 200) {

                        try {

                            for (i in 0 until response.data.result.size){
                                orderIds.add(response.data.result.get(i).id)
                            }
                            jsonObject.currencyCode = "INR"
                            jsonObject.shippingFixedAddress = ViewAddressResult
                            jsonObject.price = FINAL_AMOUNT.toInt()
                            jsonObject.orderIds = orderIds

                            viewModel.checkQuantityApi(token)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else if(response.data?.responseCode == 440){

                        supportFragmentManager.let {
                            LogOutDialog(this).show(it, "MyCustomFragment")
                        }
                    }
                }

                is Resource.Error -> {

                    ProgressBar.hideProgress()
                    response.message?.let { message ->
                        androidExtension.alertBox(message, this@CartSummeryActivity)
                    }
                }

                is Resource.Loading -> {
                    ProgressBar.showProgress(this@CartSummeryActivity)
                }

                is Resource.Empty -> {
                    ProgressBar.hideProgress()
                }

            }

        })

    }

    private fun ObserverCheckQuantityResponse() {

        lifecycleScope.launchWhenStarted {
            viewModel._checkQuantityList.collect{ response ->

                when (response) {

                    is Resource.Success -> {
                        ProgressBar.hideProgress()
                        if (response.data?.responseCode == 200) {

                            if (response.data.result.proceed){
                                viewModel.checkOutApi(token, jsonObject)
                            }else{
                                androidExtension.alertBox(response.data.responseMessage,
                                    this@CartSummeryActivity
                                )
                            }
                        }else if(response.data?.responseCode == 440){

                            supportFragmentManager.let {
                                LogOutDialog(this@CartSummeryActivity).show(it, "MyCustomFragment")
                            }
                        }
                    }

                    is Resource.Error -> {

                        ProgressBar.hideProgress()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@CartSummeryActivity)
                        }
                    }

                    is Resource.Loading -> {
                        ProgressBar.showProgress(this@CartSummeryActivity)
                    }

                    is Resource.Empty -> {
                        ProgressBar.hideProgress()
                    }

                }
            }

        }
    }



    private fun ObserveCartListResponse() {

        viewModel._cartListStateFlow.observe(this, Observer{ response ->

            when (response) {

                is Resource.Success -> {

                    ProgressBar.hideProgress()

                    try {
                        if (response.data?.responseCode == 200) {
                            var cartListData = response.data.result


                            var priceValue = 0.0
                            var shippingPrice = 0.0
                            for (i in cartListData.indices) {
                                priceValue += cartListData.get(i).totalPrice.toDouble() * cartListData.get(i).quantity!!


                                var merchantId = cartListData[i].merchantId
                                var requestActualPrice = 0.0
                                var requestDeliveryFee = 0.0
                                var requestFinalOrderPrice = 0.0
                                if (!cartListData[i].myFlag) {
                                    var cartProductId = ArrayList<String>()
                                    for (i in cartListData.indices) {
                                        if (merchantId == cartListData[i].merchantId) {
                                            cartProductId.add(cartListData[i].id)

                                            requestActualPrice += cartListData.get(i).totalPrice.toDouble() * cartListData.get(
                                                i
                                            ).quantity
                                            requestDeliveryFee += PER_PRODUCT_DELIVERY_FEE
                                            requestFinalOrderPrice =
                                                requestActualPrice + requestDeliveryFee
                                            cartListData[i].myFlag = true

                                        }
                                    }
                                    shippingPrice += requestDeliveryFee
                                    var createOrderRequest = OrderCreateRequest()
                                    createOrderRequest.merchantId = merchantId
                                    createOrderRequest.cartId = cartProductId
                                    createOrderRequest.actualPrice = requestActualPrice
                                    createOrderRequest.orderPrice = requestFinalOrderPrice
                                    createOrderRequest.deliveryFee = requestDeliveryFee
                                    createOrderRequest.shippingFixedAddress = ViewAddressResult
                                    createOrderRequest.dealsDiscount = 0
                                    createOrderRequest.taxPrice = 0
                                    finalCreateOrderRequest.orderDetails.add(createOrderRequest)
                                }
                            }
                            BAG_AMOUNT = priceValue
                            FINAL_AMOUNT = BAG_AMOUNT + shippingPrice

                            if (cartListData.size == 1){
                                binding.priceDetail.text =
                                    "Price Details (${cartListData.size} Item)"
                            }else if(cartListData.size > 1){
                                binding.priceDetail.text =
                                    "Price Details (${cartListData.size} Items)"
                            }

                            binding.summeryBagPrice.text =
                                CommonFunctions.currencyFormatter(BAG_AMOUNT)
                            binding.summeryShippingPrice.text =
                                CommonFunctions.currencyFormatter(shippingPrice)
                            binding.summeryFinalPrice.text =
                                CommonFunctions.currencyFormatter(FINAL_AMOUNT)

                            setCartListAdaptor(cartListData)

                        }else if(response.data?.responseCode == 440){

                            supportFragmentManager.let {
                                LogOutDialog(this).show(it, "MyCustomFragment")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                is Resource.Error -> {
                    ProgressBar.hideProgress()
                    if (response.data?.responseCode == 404) {

                    } else {
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@CartSummeryActivity)
                        }
                    }
                }

                is Resource.Loading -> {
//                        ProgressBar.showProgress(this@CartSummeryActivity)
                }

                is Resource.Empty -> {
                    ProgressBar.hideProgress()
                }

            }

        })
    }

    private fun ObserveViewAddressResponce() {

        viewModel._viewAddressStateFlow.observe(this,Observer { response ->

            when (response) {

                is Resource.Success -> {

                    if (response.data?.responseCode == 200) {

                        val addressData = response.data.result
                        ViewAddressResult = response.data.result
                        Log.d("whatHappen",ViewAddressResult.address)
                        binding.tvAddress.text = addressData.address
                        binding.tvPincode.text = "Pincode - ${addressData.zipCode}"
                        binding.tvName.text = addressData.name
                        binding.tvNumber.text = "${addressData.countryCode}-${addressData.mobileNumber}"
                    }else if(response.data?.responseCode == 440){

                        supportFragmentManager.let {
                            LogOutDialog(this).show(it, "MyCustomFragment")
                        }
                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        androidExtension.alertBox(message, this@CartSummeryActivity)
                    }
                }

                is Resource.Loading -> {
                    ProgressBar.showProgress(this@CartSummeryActivity)
                }

                is Resource.Empty -> {
                }

            }
        })
    }


    fun setCartListAdaptor(cartListData: List<CartListResponse.CartListResult>) {
        binding.CartRecycler.layoutManager = LinearLayoutManager(this@CartSummeryActivity)
        binding.CartRecycler.adapter = CartSummeryAdapter(this@CartSummeryActivity, cartListData)
    }

    override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {


        try {
            val jsonObject = JsonObject()

            jsonObject.addProperty("razorpay_payment_id", paymentId.toString())
            jsonObject.addProperty("razorpay_order_id", paymentData?.orderId.toString())
            jsonObject.addProperty("razorpay_signature", paymentData?.signature.toString())

            viewModel.verifyPaymentApi(jsonObject)

            ObserveVerifyPaymentResponse()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

        Log.e("paymentError",p0.toString())
        Log.e("paymentError",p1.toString())

        if (p0 == 5){
            androidExtension.alertBox("Payment cancelled",this@CartSummeryActivity)
        }else if (p0 == 1){
//            androidExtension.alertBox("Payment cancelled",this@CartSummeryActivity)
        }else if (p0 == 2){
            androidExtension.alertBox("Please check your internet connection",this@CartSummeryActivity)
        }

    }

    override fun paymentDialogClick() {

        SavedPrefManager.savePreferenceBoolean(
            this,
            SavedPrefManager.loggedIn,
            true
        )

        sharedLoginViewModel.saveToken(token)

        val intent = Intent(this@CartSummeryActivity, FragmentContainerActivity::class.java)
            intent.putExtra("paymentFlag", "payment")
        intent.putExtra("token",token)
            startActivity(intent)
            finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //        intent?.getStringExtra("flag")?.let {
//            flag = it
//        }
//        if (flag.equals("payment")) {
//            binding.view4.visibility = View.VISIBLE
//            binding.llThanksForShopping.visibility = View.VISIBLE
//            binding.view5.visibility = View.VISIBLE
//        } else {
//            binding.view4.visibility = View.GONE
//            binding.llThanksForShopping.visibility = View.GONE
//            binding.view5.visibility = View.GONE
//        }


//    override fun onDestroy() {
//        super.onDestroy()
//
//        Checkout.clearUserData(this@CartSummeryActivity)
//    }


    override fun sessionExpiredClick() {

        SavedPrefManager.savePreferenceBoolean(
            this,
            SavedPrefManager.loggedIn,
            false
        )

        Intent(this, RoleSelectScreen::class.java).also {
            startActivity(it)
            finishAffinity()
        }
    }
}