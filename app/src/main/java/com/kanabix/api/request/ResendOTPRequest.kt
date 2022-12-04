package com.kanabix.api.request

import com.google.gson.annotations.SerializedName

class ResendOTPRequest {

    @SerializedName("email")
    var email: String = ""

    @SerializedName("userType")
    var userType: String = ""
}