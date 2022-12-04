package com.kanabix.api.response

import com.google.gson.annotations.SerializedName

class PaymentViewResponse {

//    @SerializedName("result")
//    val result: PaymentListResult = PaymentListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class PaymentViewResult{

}