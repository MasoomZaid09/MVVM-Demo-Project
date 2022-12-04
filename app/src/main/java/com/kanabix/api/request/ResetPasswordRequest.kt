package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class ResetPasswordRequest {

    @SerializedName("userType")
    var userType: String = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("newPassword")
    var newPassword: String = ""
}