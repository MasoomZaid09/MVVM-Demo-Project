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
import com.kanabix.api.request.SignUpRequest
import com.kanabix.api.response.CountryStateCityListResponse
import com.kanabix.api.response.SignUpResponse
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.api.Constants.NO_INTERNET
import com.kanabix.api.response.UploadFileResponse
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CustomerSignInVIewModel @Inject constructor(
    app: Application,
    private val respository: AniketRespository
) : AndroidViewModel(app) {



    private val SignInStateFlow: MutableStateFlow<Resource<SignUpResponse>> =
        MutableStateFlow(Resource.Empty())

    val _SignInStateFlow: StateFlow<Resource<SignUpResponse>> = SignInStateFlow

    // country list
    private val CountryListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _CountryListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = CountryListStateFlow

    // state list
    private val StateListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _StateListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = StateListStateFlow

    // city list
    private val CityListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _CityListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = CityListStateFlow

    // upload file
    private val UploadFileStateFlow: MutableStateFlow<Resource<UploadFileResponse>> = MutableStateFlow(Resource.Empty())
    val _UploadFileStateFlow: StateFlow<Resource<UploadFileResponse>> = UploadFileStateFlow



    fun UploadFileApi(uploadFile: MultipartBody.Part?) = viewModelScope.launch{

        UploadFileStateFlow.value=Resource.Loading()
        if (hasInternetConnection()){
            respository.uploadDocumentApi(uploadFile)
                .catch { e->
                    UploadFileStateFlow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    UploadFileStateFlow.value = UploadFileHandle(data)
                }
        }else{
            UploadFileStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun UploadFileHandle(response: Response<UploadFileResponse>): Resource<UploadFileResponse> {
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



    fun signInApi(request: SignUpRequest) = viewModelScope.launch {
        SignInStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.CustomerSignIn(request)
                .catch { e ->
                    SignInStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    SignInStateFlow.value = SignInHandle(data)
                }
        }else{
            SignInStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

    fun SignInHandle(response: Response<SignUpResponse>): Resource<SignUpResponse> {
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


    // country list
    fun countryListApi() = viewModelScope.launch {
        CountryListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.CountryListApi()
                .catch { e ->
                    CountryListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CountryListStateFlow.value = ListHandle(data)
                }
        }else{
            CountryListStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

    // state list
    fun stateListApi(countryCode :String,name:String) = viewModelScope.launch {
        StateListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            respository.StateListApi(countryCode,name)
                .catch { e ->
                    StateListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    StateListStateFlow.value = ListHandle(data)
                }
        }else{
            StateListStateFlow.value = Resource.Error(NO_INTERNET)
        }

    }

    // city list
    fun cityListApi(countryCode: String, stateCode:String) = viewModelScope.launch {
        CityListStateFlow.value = Resource.Loading()

        if(hasInternetConnection()){

            respository.CityListApi(countryCode, stateCode)
                .catch { e ->
                    CityListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CityListStateFlow.value = ListHandle(data)
                }
        }else {
            CityListStateFlow.value = Resource.Error(NO_INTERNET)
        }
    }

    fun ListHandle(response: Response<CountryStateCityListResponse>): Resource<CountryStateCityListResponse> {
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