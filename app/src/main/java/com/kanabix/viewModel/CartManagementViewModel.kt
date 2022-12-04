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
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartManagementViewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {

    private val CartStateFlow: MutableStateFlow<Resource<CategoryManagementResponse>> =
        MutableStateFlow(Resource.Empty())

    val _CartStateFlow: StateFlow<Resource<CategoryManagementResponse>> = CartStateFlow


    fun CartManagmentApi(search:String,page:Int,limit:Int) = viewModelScope.launch {
        CartStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.cartList(search,page,limit)
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

    //    InterNet
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