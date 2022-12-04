package com.kanabix.api.request

import com.google.gson.annotations.SerializedName
import com.kanabix.api.response.LoginResponse

class ForgetPasswordRequest {

    @SerializedName("email")
    var email: String = ""

    @SerializedName("userType")
    var userType: String = ""
}