package com.kanabix.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.kanabix.KanabixApp
import com.kanabix.api.Constants
import com.kanabix.api.response.*
import com.kanabix.models.PojoClass
import com.kanabix.repository.AniketRespository
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DeliveryAddAddressViewModel @Inject
constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {
    //add address
    private val addAddressStateFlow: MutableStateFlow<Resource<AddAddressResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )
    val _addAddressStateFlow: StateFlow<Resource<AddAddressResponse>> = addAddressStateFlow


    // list address
    private val listAddressStateFlow: MutableStateFlow<Resource<AddressListResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )
    val _listAddressStateFlow: StateFlow<Resource<AddressListResponse>> = listAddressStateFlow

 // view address
    private val viewAddressStateFlow: MutableStateFlow<Resource<ViewAddressResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )
    val _viewAddressStateFlow: StateFlow<Resource<ViewAddressResponse>> = viewAddressStateFlow


    // country list
    private val CountryListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _CountryListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> =
        CountryListStateFlow

    // state list
    private val StateListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _StateListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = StateListStateFlow

    // city list
    private val CityListStateFlow: MutableStateFlow<Resource<CountryStateCityListResponse>> =
        MutableStateFlow(Resource.Empty())

    val _CityListStateFlow: StateFlow<Resource<CountryStateCityListResponse>> = CityListStateFlow

    //edit address
    private val editAddressStateFlow: MutableStateFlow<Resource<EditAddressResponse>> =
        MutableStateFlow(Resource.Empty())

    val _editAddressStateFlow: StateFlow<Resource<EditAddressResponse>> = editAddressStateFlow

    //delete address
//    private val deleteAddressStateFlow: MutableStateFlow<Resource<EditAddressResponse>> =
//        MutableStateFlow(Resource.Empty())
//
//    val _deleteAddressStateFlow: StateFlow<Resource<EditAddressResponse>> = deleteAddressStateFlow

    private val deleteAddressStateFlow: MutableSharedFlow<Resource<EditAddressResponse>> =
        MutableSharedFlow()

    val _deleteAddressStateFlow = deleteAddressStateFlow.asSharedFlow()

    //add address api
    fun addAddressApi(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        addAddressStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.AddAddressApi(token, jsonObject)
                .catch { e ->
                    addAddressStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    addAddressStateFlow.value = addAddressApiHandle(data)
                }
        } else {
            addAddressStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun addAddressApiHandle(response: Response<AddAddressResponse>): Resource<AddAddressResponse> {
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

    // list address api
    fun listAddressApi(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        listAddressStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.ListAddressApi(token, jsonObject)
                .catch { e ->
                    listAddressStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    listAddressStateFlow.value = listAddressApiHandle(data)
                }
        } else {
            addAddressStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun listAddressApiHandle(response: Response<AddressListResponse>): Resource<AddressListResponse> {
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

    //View address api
    fun viewAddressApi(token: String, addressId: String) = viewModelScope.launch {
        viewAddressStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.ViewAddressApi(token, addressId)
                .catch { e ->
                    viewAddressStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    viewAddressStateFlow.value = viewAddressApiHandle(data)
                }
        } else {
            viewAddressStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun viewAddressApiHandle(response: Response<ViewAddressResponse>): Resource<ViewAddressResponse> {
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

    // country list
    fun countryListApi() = viewModelScope.launch {
        CountryListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.CountryListApi()
                .catch { e ->
                    CountryListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CountryListStateFlow.value = ListHandle(data)
                }
        } else {
            CountryListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    // state list
    fun stateListApi(countryCode: String,name:String) = viewModelScope.launch {
        StateListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.StateListApi(countryCode,name)
                .catch { e ->
                    StateListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    StateListStateFlow.value = ListHandle(data)
                }
        } else {
            StateListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    // city list
    fun cityListApi(countryCode: String, stateCode: String) = viewModelScope.launch {
        CityListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.CityListApi(countryCode, stateCode)
                .catch { e ->
                    CityListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    CityListStateFlow.value = ListHandle(data)
                }
        } else {
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

    //add address api
    fun editAddressApi(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        editAddressStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.EditAddressApi(token, jsonObject)
                .catch { e ->
                    editAddressStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    editAddressStateFlow.value = editAddressApiHandle(data)
                }
        } else {
            editAddressStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun editAddressApiHandle(response: Response<EditAddressResponse>): Resource<EditAddressResponse> {
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


    //delete address api
    fun deleteAddressApi(token: String, _id: String) = viewModelScope.launch {
        deleteAddressStateFlow.emit(Resource.Loading())

        if (hasInternetConnection()) {

            repository.DeleteAddressApi(token, _id)
                .catch { e ->
                    deleteAddressStateFlow.emit(Resource.Error(e.message.toString()))
                }.collect { data ->
                    deleteAddressStateFlow.emit(deleteAddressApiHandle(data))
                }
        } else {
            deleteAddressStateFlow.emit(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun deleteAddressApiHandle(response: Response<EditAddressResponse>): Resource<EditAddressResponse> {
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