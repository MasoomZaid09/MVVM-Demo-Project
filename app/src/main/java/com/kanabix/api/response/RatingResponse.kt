package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class RatingResponse {
    @SerializedName("result")
    val result: RatingResult = RatingResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class RatingResult {
    @SerializedName("ratingCount")
    val ratingCount: Int = 0
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}