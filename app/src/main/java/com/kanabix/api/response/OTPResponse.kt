package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class OTPResponse {
    @SerializedName("result")
    val result: OTPResult = OTPResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class OTPResult {

    @SerializedName("_id")
    val id: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("userType")
    val userType: String = ""
    @SerializedName("otpVerification")
    val otpVerification: Boolean = false
    @SerializedName("token")
    val token: String = ""

}