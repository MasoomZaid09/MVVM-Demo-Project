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
import com.kanabix.api.response.AddWishListResponse
import com.kanabix.api.response.FaqsResponse
import com.kanabix.api.response.TermsResponse
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
class SharedStaticContentViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){


    private val termsListStateFlow: MutableStateFlow<Resource<TermsResponse>> = MutableStateFlow(Resource.Empty())
    val _termsListStateFlow : StateFlow<Resource<TermsResponse>> = termsListStateFlow

    private val faqListStateFlow: MutableStateFlow<Resource<FaqsResponse>> = MutableStateFlow(Resource.Empty())
    val _faqListStateFlow: StateFlow<Resource<FaqsResponse>> = faqListStateFlow



    fun termsListApi() = viewModelScope.launch {
        termsListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.termListApi()
                .catch { e ->
                    termsListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    termsListStateFlow.tryEmit(TermsListHandle(data))
                }
        }else{
            termsListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun TermsListHandle(response: Response<TermsResponse>): Resource<TermsResponse> {
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


    fun FaqsListApi() = viewModelScope.launch {
        faqListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.faqListApi()
                .catch { e ->
                    faqListStateFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    faqListStateFlow.tryEmit(FaqsListHandle(data))
                }
        }else{
            faqListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun FaqsListHandle(response: Response<FaqsResponse>): Resource<FaqsResponse> {
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