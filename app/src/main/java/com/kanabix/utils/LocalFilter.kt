package com.kanabix.utils

import androidx.compose.ui.text.toLowerCase
import com.kanabix.adapters.FilterAdapter
import com.kanabix.adapters.OpenPopUp
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.api.response.CountryStateCityListResult

object LocalFilter {

    fun filter(search:String, data : ArrayList<CountryStateCityListResult>, adapter :OpenPopUp){

        try {
            val filterData = ArrayList<CountryStateCityListResult>()

            for(item in data){

                data?.let {
                    if (item.name.lowercase().contains(search.lowercase())){
                        filterData.add(item)
                    }
                }
            }

            adapter.filterData(filterData)
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    fun filterCategory(search:String, data : ArrayList<CategoryManagementDoc>, adapter :FilterAdapter){

        try {

            val filterData = ArrayList<CategoryManagementDoc>()

            for (item in data) {

                data?.let {
                    if (item.categoryName.lowercase().contains(search.lowercase())) {
                        filterData.add(item)
                    }
                }
            }

            adapter.filterData(filterData)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}