package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.kanabix.KanabixApp
import com.kanabix.api.Constants
import com.kanabix.api.request.AddRatingRequest
import com.kanabix.api.response.LogOutResponse
import com.kanabix.api.response.MyProfileResponse
import com.kanabix.api.response.RatingResponse
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){

    val MyProfileFlow: MutableStateFlow<Resource<MyProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _myProfileFlow: StateFlow<Resource<MyProfileResponse>> = MyProfileFlow

    val RateAppFlow: MutableSharedFlow<Resource<RatingResponse>> = MutableSharedFlow()
    val _RateAppFlow = RateAppFlow.asSharedFlow()

    val LogOutFlow: MutableStateFlow<Resource<LogOutResponse>> = MutableStateFlow(Resource.Empty())
    val _LogOutFlow : StateFlow<Resource<LogOutResponse>> = LogOutFlow


    fun myProfile(token:String) = viewModelScope.launch {
        MyProfileFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.MyProfileApi(token)
                .catch { e ->
                    MyProfileFlow.value = Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    MyProfileFlow.tryEmit(ProfileHandle(data))
                }
        }else{
            MyProfileFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun ProfileHandle(response: Response<MyProfileResponse>): Resource<MyProfileResponse> {
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

    // Log Out
    fun logOutApi(token:String,deviceToken:String,deviceType:String) = viewModelScope.launch {
        LogOutFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.logOutApi(token,deviceToken, deviceType)
                .catch { e ->
                    LogOutFlow.value =Resource.Error(e.message.toString())
                }.collectLatest { data ->
                    LogOutFlow.value = LogOutHandle(data)
                }
        }else{
            LogOutFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun LogOutHandle(response: Response<LogOutResponse>): Resource<LogOutResponse> {
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


    fun MyRateApi(token:String,request : AddRatingRequest) = viewModelScope.launch {
        RateAppFlow.emit(Resource.Loading())

        if (hasInternetConnection()){

            repository.appRatingApi(token,request)
                .catch { e ->
                    RateAppFlow.emit(Resource.Error(e.message.toString()))
                }.collectLatest { data ->
                    RateAppFlow.emit(MyRateHandle(data))
                }
        }else{
            RateAppFlow.emit(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun MyRateHandle(response: Response<RatingResponse>): Resource<RatingResponse> {
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