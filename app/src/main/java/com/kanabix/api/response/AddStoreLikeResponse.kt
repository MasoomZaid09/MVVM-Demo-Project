package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class AddStoreLikeResponse {
    @SerializedName("result")
    val result: AddStoreLikeResult = AddStoreLikeResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class AddStoreLikeResult {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("storeId")
    val storeId: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("__v")
    val v: Int = 0
}