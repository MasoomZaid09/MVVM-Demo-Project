package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class FaqsResponse {
    @SerializedName("result")
    val result: ArrayList<FaqsResult> = ArrayList()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class FaqsResult {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("question")
    val question: String = ""
    @SerializedName("answer")
    val answer: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}