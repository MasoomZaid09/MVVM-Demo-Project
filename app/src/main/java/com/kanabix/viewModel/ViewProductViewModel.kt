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
import com.kanabix.api.response.AddWishListResponse
import com.kanabix.api.response.ProductAddToCartResponse
import com.kanabix.api.response.ViewProductResponse
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
class ViewProductViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){

    private val viewProductStateFlow: MutableStateFlow<Resource<ViewProductResponse>> = MutableStateFlow(
        Resource.Empty())
    val _viewProductStateFlow: StateFlow<Resource<ViewProductResponse>> = viewProductStateFlow


    private val productAddToCartStateFlow: MutableStateFlow<Resource<ProductAddToCartResponse>> = MutableStateFlow(
        Resource.Empty())
    val _productAddToCartStateFlow: StateFlow<Resource<ProductAddToCartResponse>> = productAddToCartStateFlow

    fun viewProductApi(token: String,productId :String) = viewModelScope.launch {
        viewProductStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.viewProductApi(token,productId)
                .catch { e ->
                    viewProductStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    viewProductStateFlow.tryEmit(viewProductHandle(data))
                }
        }else{
            viewProductStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun viewProductHandle(response: Response<ViewProductResponse>): Resource<ViewProductResponse> {
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


    fun productAddToCartApi(token:String,request:JsonObject) = viewModelScope.launch {
        productAddToCartStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.productAddToCartApi(token,request)
                .catch { e ->
                    productAddToCartStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    productAddToCartStateFlow.tryEmit(productAddToCartHandle(data))
                }
        }else{
            productAddToCartStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun productAddToCartHandle(response: Response<ProductAddToCartResponse>): Resource<ProductAddToCartResponse> {
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
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