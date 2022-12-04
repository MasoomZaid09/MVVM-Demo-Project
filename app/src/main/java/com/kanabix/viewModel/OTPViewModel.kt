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
import com.kanabix.api.request.OTPRequest
import com.kanabix.api.request.ResendOTPRequest
import com.kanabix.api.response.OTPResponse
import com.kanabix.api.response.ResendOTPResponse
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.api.Constants.NO_INTERNET
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {

    // verify otp
    private val OTPStateFlow: MutableStateFlow<Resource<OTPResponse>> = MutableStateFlow(Resource.Empty())
    val _OTPStateFlow: StateFlow<Resource<OTPResponse>> = OTPStateFlow

    // resend otp
    private val ResendOtpStateFlow: MutableStateFlow<Resource<ResendOTPResponse>> = MutableStateFlow(Resource.Empty())
    val _ResendOtpStateFlow: StateFlow<Resource<ResendOTPResponse>> = ResendOtpStateFlow


    fun OtpApi(request: OTPRequest) = viewModelScope.launch {
        OTPStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){
            respository.VerifyOtp(request)
                .catch { e ->
                    OTPStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    OTPStateFlow.value = OTPHandle(data)
                }
        }else{
            OTPStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

    fun OTPHandle(response: Response<OTPResponse>): Resource<OTPResponse> {
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


    // resend otp

    fun resendOtpApi(request: ResendOTPRequest) = viewModelScope.launch {
        ResendOtpStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.ResendOtp(request)
                .catch { e ->
                    ResendOtpStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    ResendOtpStateFlow.value = ResendOtpHandle(data)
                }
        }else{
            ResendOtpStateFlow.value = Resource.Error(NO_INTERNET)
        }
        respository.ResendOtp(request)
            .catch { e ->
                ResendOtpStateFlow.value = Resource.Error(e.message.toString())
            }.collect { data ->
                ResendOtpStateFlow.value = ResendOtpHandle(data)
            }
    }

    fun ResendOtpHandle(response: Response<ResendOTPResponse>): Resource<ResendOTPResponse> {
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