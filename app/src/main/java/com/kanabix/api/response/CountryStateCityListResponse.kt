package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class CountryStateCityListResponse {
    @SerializedName("result")
    val result: ArrayList<CountryStateCityListResult> = ArrayList()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class CountryStateCityListResult {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("isoCode")
    val isoCode: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("flag")
    val flag: String = ""
    @SerializedName("currency")
    val currency: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
}