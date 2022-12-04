package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class LocationResponse {
//    @SerializedName("result")
//    val result: LocationResult = LocationResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}
