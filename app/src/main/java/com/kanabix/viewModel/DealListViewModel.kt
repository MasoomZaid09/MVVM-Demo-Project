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
import kotlin.math.ln


@HiltViewModel
class DealListViewModel  @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){

    val dealListStateFlow: MutableStateFlow<Resource<DealListResponse>> = MutableStateFlow(Resource.Empty())
    val _dealListStateFlow: StateFlow<Resource<DealListResponse>> = dealListStateFlow

    // cart list
    private val CartStateFlow: MutableStateFlow<Resource<CategoryManagementResponse>> = MutableStateFlow(Resource.Empty())
    val _CartStateFlow: StateFlow<Resource<CategoryManagementResponse>> = CartStateFlow


    fun CartManagmentApi(search:String,page:Int,limit:Int) = viewModelScope.launch {
        CartStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.cartList(search,page,limit)
                .catch { e ->
                    CartStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CartStateFlow.value = CartManagementHandle(data)
                }
        }else {
            CartStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun CartManagementHandle(response: Response<CategoryManagementResponse>): Resource<CategoryManagementResponse> {
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

    fun dealListApi(lat:Double, lng :Double,search :String, categoryId :String,page:Int,limit:Int) = viewModelScope.launch {
        dealListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.dealListApi(lat, lng, search, categoryId,page, limit)
                .catch { e ->
                    dealListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
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