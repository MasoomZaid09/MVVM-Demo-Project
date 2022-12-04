package com.kanabix.api.response
import com.google.gson.annotations.SerializedName

class AddWishListResponse {
    @SerializedName("result")
    val result: AddWishListResult = AddWishListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class AddWishListResult {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("isLike")
    val isLike: Boolean = false
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}