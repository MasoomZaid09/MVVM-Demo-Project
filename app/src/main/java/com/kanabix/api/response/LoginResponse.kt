package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("result")
    val result: LoginResult = LoginResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class LoginResult {
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("userType")
    val userType: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("profilePic")
    val profilePic: String = ""
    @SerializedName("token")
    val token: String = ""
    @SerializedName("completeProfile")
    val completeProfile: Boolean = false
    @SerializedName("userRequestStatus")
    val userRequestStatus: String = ""
    @SerializedName("otpVerification")
    val otpVerification: Boolean = false
    @SerializedName("locationSet")
    val locationSet: Boolean = false
    @SerializedName("flag")
    val flag: Int = 0
    @SerializedName("iat")
    val iat: String = ""
    @SerializedName("exp")
    val exp: String = ""
}