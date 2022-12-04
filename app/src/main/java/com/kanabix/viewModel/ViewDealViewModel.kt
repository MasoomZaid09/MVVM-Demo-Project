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
import com.kanabix.api.request.ProductListRequest
import com.kanabix.api.response.*
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
class ViewDealViewModel @Inject constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {

    private val viewDealStateFlow: MutableStateFlow<Resource<ViewDealResponse>> = MutableStateFlow(Resource.Empty())
    val _viewDealStateFlow: StateFlow<Resource<ViewDealResponse>> = viewDealStateFlow


    //DEAL ADD TO CART
    private val productAddToCartStateFlow: MutableStateFlow<Resource<ProductAddToCartResponse>> = MutableStateFlow(Resource.Empty())
    val _productAddToCartStateFlow: StateFlow<Resource<ProductAddToCartResponse>> = productAddToCartStateFlow


    // suggested product list
    val productListStateFlow: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val _productListStateFlow: StateFlow<Resource<ProductListResponse>> = productListStateFlow


    fun productList(token:String,request : ProductListRequest) = viewModelScope.launch {
        productListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.ProductListApi(token,request)
                .catch { e ->
                    productListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    productListStateFlow.tryEmit(ProductListHandle(data))
                }
        }else{
            productListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun ProductListHandle(response: Response<ProductListResponse>): Resource<ProductListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }
//        val errorMsg = JSONObject(response.errorBody()!!.charStream().readText())
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    fun viewDealApi(token: String,dealId: String) = viewModelScope.launch {
        viewDealStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.viewDealApi(token,dealId)
                .catch { e ->
                    viewDealStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    viewDealStateFlow.tryEmit(viewDealHandle(data))
                }
        } else {
            viewDealStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun viewDealHandle(response: Response<ViewDealResponse>): Resource<ViewDealResponse> {
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

    //DEAL ADD TO CART
    fun dealAddToCartApi(token: String, request: JsonObject) = viewModelScope.launch {
        productAddToCartStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.productAddToCartApi(token, request)
                .catch { e ->
                    productAddToCartStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    productAddToCartStateFlow.tryEmit(dealAddToCartHandle(data))
                }
        } else {
            productAddToCartStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun dealAddToCartHandle(response: Response<ProductAddToCartResponse>): Resource<ProductAddToCartResponse> {
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