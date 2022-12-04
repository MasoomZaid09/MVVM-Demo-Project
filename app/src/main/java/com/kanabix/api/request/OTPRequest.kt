package com.kanabix.api.request

import com.google.gson.annotations.SerializedName
import com.kanabix.api.response.LoginResponse

class OTPRequest {

    @SerializedName("email")
    var email: String = ""

    @SerializedName("otp")
    var otp: String = ""

    @SerializedName("userType")
    var userType: String = ""
}