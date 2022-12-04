package com.kanabix.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.parser.moshi.JsonReader.Token
import com.kanabix.repository.AniketRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedLoginViewModel @Inject constructor(app : Application, private val repository:AniketRespository) : AndroidViewModel(app) {


    private val sharedTokenState = MutableLiveData<String>()
    val _sharedTokenState :LiveData<String> = sharedTokenState


    fun saveToken(token : String){

        sharedTokenState.value = token
    }

}