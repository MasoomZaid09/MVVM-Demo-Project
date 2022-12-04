package com.kanabix.api.request

import com.google.gson.annotations.SerializedName

class LocationRequest
{
    @SerializedName("token")
    var token: String = ""

    @SerializedName("latitude")
    var latitude: Double = 0.0
    @SerializedName("longitude")
    var longitude: Double = 0.0
}