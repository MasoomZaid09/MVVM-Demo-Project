package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class BannerResponse {
    @SerializedName("result")
    val result: ArrayList<BannerResult> = ArrayList()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class BannerResult {
    @SerializedName("bannerImage")
    val bannerImage: ArrayList<String> = ArrayList()
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("bannerName")
    val bannerName: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("thumbNail")
    val thumbNail: String = ""
}