package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class checkQuantityPaymentResponse {

    @SerializedName("result")
    val result: checkQuantityPaymentResult = checkQuantityPaymentResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class checkQuantityPaymentResult {
    @SerializedName("proceed")
    val proceed: Boolean = false
}