package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
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
class CartViewModel @Inject
constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {

    private val cartListStateFlow: MutableStateFlow<Resource<CartListResponse>> = MutableStateFlow(Resource.Empty())

    private val deleteCartItemStateFlow: MutableStateFlow<Resource<DeleteCartItemResponse>> = MutableStateFlow(Resource.Empty())

    private val updateCartItemStateFlow: MutableStateFlow<Resource<UpdateCartItemResponse>> = MutableStateFlow(Resource.Empty())

    val _cartListStateFlow: StateFlow<Resource<CartListResponse>> = cartListStateFlow

    val _deleteCartItemStateFlow: StateFlow<Resource<DeleteCartItemResponse>> = deleteCartItemStateFlow

    val _updateCartItemStateFlow: StateFlow<Resource<UpdateCartItemResponse>> = updateCartItemStateFlow

    // view address
    private val viewAddressStateFlow: MutableStateFlow<Resource<ViewAddressResponse>> = MutableStateFlow(Resource.Empty())
    val _viewAddressStateFlow: StateFlow<Resource<ViewAddressResponse>> = viewAddressStateFlow




    fun cartListApi(token: String) = viewModelScope.launch {
        cartListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.CartListApi(token)
                .catch { e ->
                    cartListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    cartListStateFlow.value = CartListApiHandle(data)
                }
        } else {
            cartListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
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

    fun deleteCartItemApi(token: String, _id : String) = viewModelScope.launch {
        deleteCartItemStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.DeleteCartItemApi(token, _id)
                .catch { e ->
                    deleteCartItemStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    deleteCartItemStateFlow.value = deleteCartItemApiHandle(data)
                }
        } else {
            deleteCartItemStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun deleteCartItemApiHandle(response: Response<DeleteCartItemResponse>): Resource<DeleteCartItemResponse> {
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

    fun updateCartItemApi(token: String, _id : String, jsonObject : JsonObject) = viewModelScope.launch {
        updateCartItemStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.updateCartItemApi(token, _id, jsonObject)
                .catch { e ->
                    updateCartItemStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    updateCartItemStateFlow.value = updateCartItemApiHandle(data)
                }
        } else {
            updateCartItemStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun updateCartItemApiHandle(response: Response<UpdateCartItemResponse>): Resource<UpdateCartItemResponse> {
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
        viewAddressStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.ViewAddressApi(token, addressId)
                .catch { e ->
                    viewAddressStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewAddressStateFlow.value = viewAddressApiHandle(data)
                }
        } else {
            viewAddressStateFlow.value = Resource.Error(Constants.NO_INTERNET)
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