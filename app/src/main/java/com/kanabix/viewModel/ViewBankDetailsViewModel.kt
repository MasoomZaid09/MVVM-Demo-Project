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
import com.kanabix.api.request.EditBankDetailsRequest
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
class ViewBankDetailsViewModel @Inject constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {

    // view bank details
    private val viewBankDetailsStateFlow: MutableStateFlow<Resource<ViewBankDetailsResponse>> = MutableStateFlow(
        Resource.Empty())
    val _viewBankDetailsStateFlow: StateFlow<Resource<ViewBankDetailsResponse>> = viewBankDetailsStateFlow


    // edit bank details
    private val editBankDetailsStateFlow: MutableStateFlow<Resource<EditBankDetailsResponse>> = MutableStateFlow(
        Resource.Empty())
    val _editBankDetailsStateFlow: StateFlow<Resource<EditBankDetailsResponse>> = editBankDetailsStateFlow



    fun viewBankDetails(token:String) = viewModelScope.launch {
        viewBankDetailsStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.viewBankDetails(token)
                .catch { e ->
                    viewBankDetailsStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    viewBankDetailsStateFlow.tryEmit(ViewBankDetailsHandle(data))
                }
        }else{
            viewBankDetailsStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun ViewBankDetailsHandle(response: Response<ViewBankDetailsResponse>): Resource<ViewBankDetailsResponse> {
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


    // Edit Bank Details
    fun editBankDetails(token:String,request: EditBankDetailsRequest) = viewModelScope.launch {
        editBankDetailsStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.editBankDetails(token,request)
                .catch { e ->
                    editBankDetailsStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    editBankDetailsStateFlow.tryEmit(EditBankDetailsHandle(data))
                }
        }else{
            editBankDetailsStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun EditBankDetailsHandle(response: Response<EditBankDetailsResponse>): Resource<EditBankDetailsResponse> {
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