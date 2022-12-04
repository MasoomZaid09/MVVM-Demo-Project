package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class UploadFileResponse {
    @SerializedName("result")
    val result: UploadFileResult = UploadFileResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class UploadFileResult {
    @SerializedName("mediaUrl")
    val mediaUrl: String = ""
    @SerializedName("mediaType")
    val mediaType: String = ""
    @SerializedName("thumbnail")
    val thumbnail: String = ""
}