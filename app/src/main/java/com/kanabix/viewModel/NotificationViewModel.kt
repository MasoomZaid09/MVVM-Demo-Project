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
class NotificationViewModel @Inject
constructor(
    app: Application,
    private val repository: AniketRespository
) : AndroidViewModel(app) {

    // notification list
    private val notificationListStateFlow: MutableStateFlow<Resource<NotificationListResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )

    val _notificationListStateFlow: StateFlow<Resource<NotificationListResponse>> = notificationListStateFlow

    // delete notification
    private val notificationDeleteStateFlow: MutableSharedFlow<Resource<NotificationDeleteResponse>> =
        MutableSharedFlow(
        )

    val _notificationDeleteStateFlow = notificationDeleteStateFlow.asSharedFlow()

    // clear all notification
    private val notificationClearAllStateFlow: MutableStateFlow<Resource<NotificationDeleteResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )

    val _notificationClearAllStateFlow: StateFlow<Resource<NotificationDeleteResponse>> = notificationClearAllStateFlow

    // view notification
    private val notificationViewStateFlow: MutableStateFlow<Resource<NotificationViewResponse>> =
        MutableStateFlow(
            Resource.Empty()
        )

    val _notificationViewStateFlow: StateFlow<Resource<NotificationViewResponse>> = notificationViewStateFlow

    // notification list
    fun notificationListApi(token: String) = viewModelScope.launch {
        notificationListStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.NotificationListApi(token)
                .catch { e ->
                    notificationListStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    notificationListStateFlow.value = notificationListApiHandle(data)
                }
        } else {
            notificationListStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun notificationListApiHandle(response: Response<NotificationListResponse>): Resource<NotificationListResponse> {
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


    // delete notification
    fun notificationDeleteApi(token: String, _id: String) = viewModelScope.launch {
        notificationDeleteStateFlow.emit(Resource.Loading())

        if (hasInternetConnection()) {

            repository.NotificationDeleteApi(token, _id)
                .catch { e ->
                    notificationDeleteStateFlow.emit(Resource.Error(e.message.toString()))
                }.collect { data ->
                    notificationDeleteStateFlow.emit(notificationDeleteApiHandle(data))
                }
        } else {
            notificationDeleteStateFlow.emit(Resource.Error(Constants.NO_INTERNET))
        }
    }

    fun notificationDeleteApiHandle(response: Response<NotificationDeleteResponse>): Resource<NotificationDeleteResponse> {
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


    // clear all notification
    fun notificationClearAllApi(token: String) = viewModelScope.launch {
        notificationClearAllStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.NotificationClearAllApi(token)
                .catch { e ->
                    notificationClearAllStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    notificationClearAllStateFlow.value = notificationClearAllApiHandle(data)
                }
        } else {
            notificationClearAllStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun notificationClearAllApiHandle(response: Response<NotificationDeleteResponse>): Resource<NotificationDeleteResponse> {
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

    // view notification
    fun notificationViewApi(token: String, _id: String) = viewModelScope.launch {
        notificationViewStateFlow.value = Resource.Loading()

        if (hasInternetConnection()) {

            repository.NotificationViewApi(token,_id)
                .catch { e ->
                    notificationViewStateFlow.value = Resource.Error(e.message.toString())
                }.collect { data ->
                    notificationViewStateFlow.value = notificationViewApiHandle(data)
                }
        } else {
            notificationViewStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun notificationViewApiHandle(response: Response<NotificationViewResponse>): Resource<NotificationViewResponse> {
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