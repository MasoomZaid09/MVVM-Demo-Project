package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.kanabix.KanabixApp
import com.kanabix.api.Constants
import com.kanabix.api.response.CategoryManagementResponse
import com.kanabix.api.response.DeliveryOrderListResponse
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DeliveryPartnerOrderListViewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {

    private val OrderListStateFlow: MutableStateFlow<Resource<DeliveryOrderListResponse>> = MutableStateFlow(Resource.Empty())
    val _OrderListStateFlow: StateFlow<Resource<DeliveryOrderListResponse>> = OrderListStateFlow

    fun deliveryPartnerOrderListApi(token: String, deliveryPartnerStatus: String, search: String) = viewModelScope.launch {
        OrderListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {
            respository.deliveryPartnerOrderList(token, deliveryPartnerStatus, search)
                .catch { e ->
                    OrderListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    OrderListStateFlow.value = DeliveryPartnerOrderListHandle(data)
                }
        } else {
            OrderListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun DeliveryPartnerOrderListHandle(response: Response<DeliveryOrderListResponse>): Resource<DeliveryOrderListResponse> {
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

    //    Internet
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