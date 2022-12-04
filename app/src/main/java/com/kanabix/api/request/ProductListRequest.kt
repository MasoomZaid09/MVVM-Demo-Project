package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class ProductListRequest {

    @SerializedName("page")
    var page: Int = 0
    @SerializedName("limit")
    var limit: Int = 0
    @SerializedName("lat")
    var lat: Double = 0.0
    @SerializedName("lng")
    var lng: Double = 0.0
    @SerializedName("search")
    var search: String = ""
    @SerializedName("categoryId")
    var categoryId: ArrayList<String> = ArrayList()
}