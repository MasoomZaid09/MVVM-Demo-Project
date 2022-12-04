package com.kanabix.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kanabix.repository.AniketRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchOrderViewModel @Inject constructor(
    app : Application,
    private val repository : AniketRespository
): AndroidViewModel(app){

    private val SearchOrderList : MutableLiveData<String> = MutableLiveData()
    var _SearchOrderList : LiveData<String> = SearchOrderList

    fun saveSearchText(searchText : String){
        SearchOrderList.value = searchText
    }


}