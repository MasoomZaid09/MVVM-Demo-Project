package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.content.SyncRequest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.kanabix.KanabixApp
import com.kanabix.api.Constants
import com.kanabix.api.request.CreateOrderTrackingRequest
import com.kanabix.api.response.*
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class OrderDetailsCommonViewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {

//    private val AcceptRejectOrderStateFlow: MutableStateFlow<Resource<AcceptDeclineOrderResponse>> =
//        MutableStateFlow(Resource.Empty())
//
//    val _AcceptRejectOrderStateFlow: StateFlow<Resource<AcceptDeclineOrderResponse>> = AcceptRejectOrderStateFlow

    private val AcceptRejectOrderStateFlow: MutableSharedFlow<Resource<AcceptDeclineOrderResponse>> =
        MutableSharedFlow()

    val _AcceptRejectOrderStateFlow = AcceptRejectOrderStateFlow.asSharedFlow()

    // view Order
    private val ViewOrderStateFlow: MutableStateFlow<Resource<ViewOrderResponse>> = MutableStateFlow(Resource.Empty())
    val _ViewOrderStateFlow: StateFlow<Resource<ViewOrderResponse>> = ViewOrderStateFlow

    // generate Order tracking
    private val OrderTrackingFlow: MutableStateFlow<Resource<CreateOrderTrackingResponse>> = MutableStateFlow(Resource.Empty())
    val _OrderTrackingFlow =  OrderTrackingFlow.asStateFlow()

    // generate Order tracking
    private val OtpVerifyStateFlow: MutableStateFlow<Resource<VerifyOrderTrackingResponse>> = MutableStateFlow(Resource.Empty())
    val _OtpVerifyStateFlow =  OtpVerifyStateFlow.asStateFlow()

    // generate Order tracking
    private val OtpResendStateFlow: MutableStateFlow<Resource<ResendOrderOtpResponse>> = MutableStateFlow(Resource.Empty())
    val _OtpResendStateFlow =  OtpResendStateFlow.asStateFlow()


    // otp send
    fun OtpVerifyApi(token: String, orderId: String, otp: String) = viewModelScope.launch {
        OtpVerifyStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            respository.orderOTPVerify(token, orderId, otp)
                .catch { e ->
                    OtpVerifyStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    OtpVerifyStateFlow.value = OtpSendHandle(data)
                }
        } else {
            OtpVerifyStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun OtpSendHandle(response: Response<VerifyOrderTrackingResponse>): Resource<VerifyOrderTrackingResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }

        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    // resend otp
    fun OtpResendApi(token: String, orderId: String) = viewModelScope.launch {
        OtpResendStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            respository.orderOTPResend(token, orderId)
                .catch { e ->
                    OtpResendStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    OtpResendStateFlow.value = OtpResendHandle(data)
                }
        } else {
            OtpResendStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun OtpResendHandle(response: Response<ResendOrderOtpResponse>): Resource<ResendOrderOtpResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }

        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // accept or reject
    fun acceptRejectOrderApi(token: String, orderId: String, deliveryPartnerStatus: String,reason : String,pickupDate: String?, deliveryDateAndTime: String?) = viewModelScope.launch {

        AcceptRejectOrderStateFlow.emit(Resource.Loading())

        if (hasInternetConnection()) {
            respository.acceptRejectOrderApi(token, orderId, deliveryPartnerStatus,reason,pickupDate, deliveryDateAndTime)
                .catch { e ->
                    AcceptRejectOrderStateFlow.emit(Resource.Error(e.message.toString()))
                }.collect { data ->
                    AcceptRejectOrderStateFlow.emit(AcceptRejectOrderApiHandle(data))
                }
        } else {
            AcceptRejectOrderStateFlow.emit(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun AcceptRejectOrderApiHandle(response: Response<AcceptDeclineOrderResponse>): Resource<AcceptDeclineOrderResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }

        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    // order tracking
    fun createOrderTrackingApi(token: String, request: CreateOrderTrackingRequest) = viewModelScope.launch {
        OrderTrackingFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            respository.createOrderTrackingApi(token,request)
                .catch { e ->
                    OrderTrackingFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    OrderTrackingFlow.value = createOrderTrackingHandle(data)
                }
        } else {
            OrderTrackingFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    fun createOrderTrackingHandle(response: Response<CreateOrderTrackingResponse>): Resource<CreateOrderTrackingResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }

        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    // view order api
    fun viewOrderDeliveryApi(token: String,orderId :String) = viewModelScope.launch {
        ViewOrderStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {
            respository.viewOrderDeliveryApi(token,orderId)
                .catch { e ->
                    ViewOrderStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    ViewOrderStateFlow.value = viewOrderDeliveryHandle(data)
                }
        } else {
            ViewOrderStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    fun viewOrderDeliveryHandle(response: Response<ViewOrderResponse>): Resource<ViewOrderResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    //    InterNet
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<KanabixApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}