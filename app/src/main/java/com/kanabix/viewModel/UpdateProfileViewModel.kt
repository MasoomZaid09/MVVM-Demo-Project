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
import com.kanabix.api.response.CountryStateCityListResponse
import com.kanabix.api.response.MyProfileResponse
import com.kanabix.api.response.UpdateProfileResponse
import com.kanabix.api.response.UploadFileResponse
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import com.kanabix.utils.SavedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){
init {
    GETPROFILE()
}
    private  val getprofileflow:MutableSharedFlow<Resource<MyProfileResponse>> = MutableSharedFlow()
    val _getprofile = getprofileflow.asSharedFlow()

    private val UpdateProfileFlow: MutableStateFlow<Resource<UpdateProfileResponse>> = MutableStateFlow(Resource.Empty())
    val _UpdateProfileFlow: StateFlow<Resource<UpdateProfileResponse>> = UpdateProfileFlow

    // country list
    private val CountryListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> = MutableStateFlow(Resource.Empty())
    val _CountryListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = CountryListStateFlow

    // state list
    private val StateListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> = MutableStateFlow(Resource.Empty())
    val _StateListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = StateListStateFlow

    // city list
    private val CityListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> = MutableStateFlow(Resource.Empty())
    val _CityListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = CityListStateFlow

    // upload file
    private val UploadFileStateFlow: MutableStateFlow<Resource<UploadFileResponse>> = MutableStateFlow(Resource.Empty())
    val _UploadFileStateFlow: StateFlow<Resource<UploadFileResponse>> = UploadFileStateFlow


    // upload document
    private val UploadDocumentStateFlow: MutableStateFlow<Resource<UploadFileResponse>> = MutableStateFlow(Resource.Empty())
    val _UploadDocumentStateFlow: StateFlow<Resource<UploadFileResponse>> = UploadDocumentStateFlow



    fun UploadFileApi(uploadFile: MultipartBody.Part?) = viewModelScope.launch{

        UploadFileStateFlow.value=Resource.Loading()
        if (hasInternetConnection()){
            repository.uploadFileApi(uploadFile)
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


    // upload document
    fun UploadDocumentApi(uploadFile: MultipartBody.Part?) = viewModelScope.launch{

        UploadDocumentStateFlow.value=Resource.Loading()
        if (hasInternetConnection()){
            repository.uploadDocumentApi(uploadFile)
                .catch { e->
                    UploadDocumentStateFlow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    UploadDocumentStateFlow.value = UploadDocumentHandle(data)
                }
        }else{
            UploadDocumentStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun UploadDocumentHandle(response: Response<UploadFileResponse>): Resource<UploadFileResponse> {
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




    private fun GETPROFILE() = viewModelScope.launch{

        getprofileflow.emit(Resource.Loading())

        if (hasInternetConnection()){
            repository.MyProfileApi(SavedPrefManager.getStringPreferences(getApplication(), SavedPrefManager.TOKEN)!!)
                .catch { e->
                    getprofileflow.emit(Resource.Error(e.message.toString()))
                }.collectLatest { data ->
                    getprofileflow.emit(getprofileflowHandle(data))
                }
        }else{
            getprofileflow.emit(Resource.Error(Constants.NO_INTERNET))
        }

    }

    private fun getprofileflowHandle(response: Response<MyProfileResponse>): Resource<MyProfileResponse> {
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

    fun updateProfile(token:String, request: JsonObject) = viewModelScope.launch {
        UpdateProfileFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.updateProfileApi(token,request)
                .catch { e ->
                    UpdateProfileFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    UpdateProfileFlow.value = UpdateProfileHandle(data)
                }
        }else{
            UpdateProfileFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    fun UpdateProfileHandle(response: Response<UpdateProfileResponse>): Resource<UpdateProfileResponse> {
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

            repository.CountryListApi()
                .catch { e ->
                    CountryListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CountryListStateFlow.value = ListHandle(data)
                }
        }else{
            CountryListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    // state list
    fun stateListApi(countryCode :String,name:String) = viewModelScope.launch {
        StateListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()){

            repository.StateListApi(countryCode,name)
                .catch { e ->
                    StateListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    StateListStateFlow.value = ListHandle(data)
                }
        }else{
            StateListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    // city list
    fun cityListApi(countryCode: String, stateCode:String) = viewModelScope.launch {
        CityListStateFlow.value = Resource.Loading()

        if(hasInternetConnection()){

            repository.CityListApi(countryCode, stateCode)
                .catch { e ->
                    CityListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CityListStateFlow.value = ListHandle(data)
                }
        }else {
            CityListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
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