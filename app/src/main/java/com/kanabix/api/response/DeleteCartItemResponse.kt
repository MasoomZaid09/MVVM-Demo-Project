package com.kanabix.api.response


import com.google.gson.annotations.SerializedName


class DeleteCartItemResponse {
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}