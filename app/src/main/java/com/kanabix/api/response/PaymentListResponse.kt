package com.kanabix.api.response

import com.google.gson.annotations.SerializedName

class PaymentListResponse {

//    @SerializedName("result")
//    val result: PaymentListResult = PaymentListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class PaymentListResult{

}