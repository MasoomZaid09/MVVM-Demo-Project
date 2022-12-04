package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class LogOutResponse {
    @SerializedName("result")
    val result: LogOutResult = LogOutResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
    class LogOutResult {
        @SerializedName("loggedOut")
        val loggedOut: Boolean = false
    }
}