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
class HomeViewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {

    // category list
    private val CategoryStateFlow: MutableStateFlow<Resource<CategoryManagementResponse>> = MutableStateFlow(Resource.Empty())
    val _CategoryStateFlow: StateFlow<Resource<CategoryManagementResponse>> = CategoryStateFlow

    // deal list
    val dealListStateFlow: MutableStateFlow<Resource<DealListResponse>> = MutableStateFlow(Resource.Empty())
    val _dealListStateFlow: StateFlow<Resource<DealListResponse>> = dealListStateFlow

    // banner list
    val bannerListStateFlow: MutableStateFlow<Resource<BannerResponse>> = MutableStateFlow(Resource.Empty())
    val _bannerListStateFlow: StateFlow<Resource<BannerResponse>> = bannerListStateFlow

    // product list
    val productListStateFlow: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val _productListStateFlow: StateFlow<Resource<ProductListResponse>> = productListStateFlow



    // category list api

    fun CategoryManagmentApi(search:String,page:Int,limit:Int) = viewModelScope.launch {
        CategoryStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.cartList(search,page,limit)
                .catch { e ->
                    CategoryStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CategoryStateFlow.value = CategoryManagementHandle(data)
                }
        }else {
            CategoryStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun CategoryManagementHandle(response: Response<CategoryManagementResponse>): Resource<CategoryManagementResponse> {
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


    // Deal list api

    fun DealListApi(lat:Double, lng :Double,search :String, categoryId :String,page:Int,limit:Int) = viewModelScope.launch {
        dealListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.dealListApi(lat, lng, search, categoryId,page, limit)
                .catch { e ->
                    dealListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    dealListStateFlow.tryEmit(DealListHandle(data))
                }
        }else{
            dealListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun DealListHandle(response: Response<DealListResponse>): Resource<DealListResponse> {
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

    // Banner list api

    fun BannerListApi() = viewModelScope.launch {
        bannerListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.bannerListApi()
                .catch { e ->
                    bannerListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    bannerListStateFlow.tryEmit(BannerListHandle(data))
                }
        }else{
            dealListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun BannerListHandle(response: Response<BannerResponse>): Resource<BannerResponse> {
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

    // Product List api

    fun ProductListApi(token:String,request : ProductListRequest) = viewModelScope.launch {
        productListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.ProductListApi(token,request)
                .catch { e ->
                    productListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
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


    //    Internet
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