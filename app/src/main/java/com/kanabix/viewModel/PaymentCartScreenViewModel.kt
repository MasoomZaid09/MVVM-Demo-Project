package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.kanabix.KanabixApp
import com.kanabix.api.Constants
import com.kanabix.api.request.FinalCreateOrderRequest
import com.kanabix.api.request.checkOutRequest
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
class PaymentCartScreenViewModel @Inject
constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {

    private val cartListStateFlow: MutableLiveData<Resource<CartListResponse>> = MutableLiveData()
    val _cartListStateFlow: LiveData<Resource<CartListResponse>> = cartListStateFlow

    // view address
    private val viewAddressStateFlow: MutableLiveData<Resource<ViewAddressResponse>> = MutableLiveData()
    val _viewAddressStateFlow: LiveData<Resource<ViewAddressResponse>> = viewAddressStateFlow

    // order create
    private val orderCreateStateFlow: MutableLiveData<Resource<OrderCreateResponse>> = MutableLiveData()
    val _orderCreateStateFlow: LiveData<Resource<OrderCreateResponse>> = orderCreateStateFlow

    //check out api
    private val checkOutStateFlow : MutableLiveData<Resource<checkOutResponse>> = MutableLiveData(Resource.Empty())
    val _checkOutStateFlow : LiveData<Resource<checkOutResponse>> = checkOutStateFlow

    //verify payment api
    private val verifyPaymentStateFlow : MutableLiveData<Resource<VerifyPaymentResponse>> = MutableLiveData()
    val _verifyPaymentStateFlow : LiveData<Resource<VerifyPaymentResponse>> = verifyPaymentStateFlow

    // check quantity payment
    private val checkQuantityList : MutableSharedFlow<Resource<checkQuantityPaymentResponse>> = MutableSharedFlow()
    val _checkQuantityList = checkQuantityList.asSharedFlow()



    // check out api
    fun checkOutApi(token: String, jsonObject: checkOutRequest) = viewModelScope.launch {
        checkOutStateFlow.postValue(Resource.Loading())

        if (hasInternetConnection()) {

            repository.checkOutApi(token, jsonObject)
                .catch { e ->
                    checkOutStateFlow.postValue(Resource.Error(e.message.toString()))
                }.collect { data ->
                    checkOutStateFlow.postValue(checkOutApiHandle(data))
                }
        } else {
            checkOutStateFlow.postValue(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun checkOutApiHandle(response: Response<checkOutResponse>): Resource<checkOutResponse> {
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

    // check quanity api
    fun checkQuantityApi(token: String) = viewModelScope.launch {
        checkQuantityList.emit(Resource.Loading())

        if (hasInternetConnection()) {

            repository.checkQuantityApi(token)
                .catch { e ->
                    checkQuantityList.emit(Resource.Error(e.message.toString()))
                }.collect { data ->
                    checkQuantityList.emit(checkQuantityHandle(data))
                }
        } else {
            checkQuantityList.emit(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun checkQuantityHandle(response: Response<checkQuantityPaymentResponse>): Resource<checkQuantityPaymentResponse> {
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


    // verify payment api
    fun verifyPaymentApi(jsonObject: JsonObject) = viewModelScope.launch {
        verifyPaymentStateFlow.postValue(Resource.Loading())

        if (hasInternetConnection()) {

            repository.verifyPaymentApi(jsonObject)
                .catch { e ->
                    verifyPaymentStateFlow.postValue(Resource.Error(e.message.toString()))
                }.collect { data ->
                    verifyPaymentStateFlow.postValue(verifyPaymentApiHandle(data))
                }
        } else {
            verifyPaymentStateFlow.postValue(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun verifyPaymentApiHandle(response: Response<VerifyPaymentResponse>): Resource<VerifyPaymentResponse> {
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


    fun cartListApi(token: String) = viewModelScope.launch {
        cartListStateFlow.postValue(Resource.Loading())

        if (hasInternetConnection()) {

            repository.CartListApi(token)
                .catch { e ->
                    cartListStateFlow.postValue(Resource.Error(e.message.toString()))
                }.collect { data ->
                    cartListStateFlow.postValue(CartListApiHandle(data))
                }
        } else {
            cartListStateFlow.postValue(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun CartListApiHandle(response: Response<CartListResponse>): Resource<CartListResponse> {
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


    //View address api
    fun viewAddressApi(token: String, addressId: String) = viewModelScope.launch {
        viewAddressStateFlow.postValue(Resource.Loading())

        if (hasInternetConnection()) {

            repository.ViewAddressApi(token, addressId)
                .catch { e ->
                    viewAddressStateFlow.postValue(Resource.Error(e.message.toString()))
                }.collect { data ->
                    viewAddressStateFlow.postValue(viewAddressApiHandle(data))
                }
        } else {
            viewAddressStateFlow.postValue(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun viewAddressApiHandle(response: Response<ViewAddressResponse>): Resource<ViewAddressResponse> {
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


    //create order api
    fun orderCreateApi(token: String, jsonObject: FinalCreateOrderRequest) = viewModelScope.launch {
        orderCreateStateFlow.postValue(Resource.Loading())

        if (hasInternetConnection()) {

            repository.OrderCreateApi(token, jsonObject)
                .catch { e ->
                    orderCreateStateFlow.postValue(Resource.Error(e.message.toString()))
                }.collect { data ->
                    orderCreateStateFlow.postValue(orderCreateApiHandle(data))
                }
        } else {
            orderCreateStateFlow.postValue(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun orderCreateApiHandle(response: Response<OrderCreateResponse>): Resource<OrderCreateResponse> {
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