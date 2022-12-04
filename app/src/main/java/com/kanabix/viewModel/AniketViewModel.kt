package com.kanabix.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kanabix.repository.AniketRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AniketViewModel @Inject constructor(app: Application, private val respository: AniketRespository) : AndroidViewModel(app){


//    private val postStateFlow: MutableStateFlow<Resource<RoleSelectResponce>> = MutableStateFlow(Resource.Empty())
//
//    val _postStateFlow: StateFlow<Resource<RoleSelectResponce>> = postStateFlow
//
//    fun setRole(request : RoleSelectionRequest) = viewModelScope.launch {
//        postStateFlow.value = Resource.Loading()
//        respository.setRole(request)
//            .catch { e->
//                postStateFlow.value=Resource.Error(e.message.toString())
//            }.collect { data->
//                postStateFlow.value= AddressListHandle(data)
//            }
//    }
//


}