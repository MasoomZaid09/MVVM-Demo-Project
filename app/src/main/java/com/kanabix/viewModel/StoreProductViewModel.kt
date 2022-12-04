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
import com.kanabix.api.request.StoreListRequest
import com.kanabix.api.response.StoreListResponse
import com.kanabix.api.response.StoreProductsListResponse
import com.kanabix.api.response.ViewStoreResponse
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
class StoreProductViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){

    // store view
    val StoreViewStateFlow: MutableStateFlow<Resource<ViewStoreResponse>> = MutableStateFlow(Resource.Empty())
    val _StoreViewStateFlow: StateFlow<Resource<ViewStoreResponse>> = StoreViewStateFlow

    // store product list
    val StoreProductListStateFlow: MutableStateFlow<Resource<StoreProductsListResponse>> = MutableStateFlow(Resource.Empty())
    val _StoreProductListStateFlow: StateFlow<Resource<StoreProductsListResponse>> = StoreProductListStateFlow

    // store view api
    fun StoreViewApi(token : String,storeId: String) = viewModelScope.launch {

        StoreViewStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.viewStoreApi(token,storeId)
                .catch { e ->
                    StoreViewStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    StoreViewStateFlow.tryEmit(StoreViewHandle(data))
                }
        }else{
            StoreViewStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun StoreViewHandle(response: Response<ViewStoreResponse>): Resource<ViewStoreResponse> {
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


    // store product list api
    fun StoreProductListApi(token: String, storeId :String,search:String, page:Int,limit: Int) = viewModelScope.launch {

        StoreProductListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.storeProductListApi(token,storeId,search,page,limit)
                .catch { e ->
                    StoreProductListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    StoreProductListStateFlow.tryEmit(StoreProductListHandle(data))
                }
        }else{
            StoreProductListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun StoreProductListHandle(response: Response<StoreProductsListResponse>): Resource<StoreProductsListResponse> {
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