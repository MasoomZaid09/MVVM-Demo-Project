package com.kanabix.api.response


import com.google.gson.annotations.SerializedName


class AddressListResponse {
    @SerializedName("result")
    val result: AddressListResult = AddressListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
    class AddressListResult {
        @SerializedName("docs")
        val docs: ArrayList<AddressListDoc> = ArrayList()
        @SerializedName("total")
        val total: Int = 0
        @SerializedName("limit")
        val limit: Int = 0
        @SerializedName("page")
        val page: Int = 0
        @SerializedName("pages")
        val pages: Int = 0
        class AddressListDoc {
            @SerializedName("isPrimary")
            val isPrimary: Boolean = false
            @SerializedName("status")
            val status: String = ""
            @SerializedName("_id")
            val id: String = ""
            @SerializedName("address")
            val address: String = ""
            @SerializedName("zipCode")
            val zipCode: String = ""
            @SerializedName("state")
            val state: String = ""
            @SerializedName("city")
            val city: String = ""
            @SerializedName("country")
            val country: String = ""
            @SerializedName("name")
            val name: String = ""
            @SerializedName("mobileNumber")
            val mobileNumber: String = ""
            @SerializedName("countryCode")
            val countryCode: String = ""
            @SerializedName("userId")
            val userId: String = ""
            @SerializedName("createdAt")
            val createdAt: String = ""
            @SerializedName("updatedAt")
            val updatedAt: String = ""
            @SerializedName("__v")
            val v: Int = 0
            @SerializedName("email")
            val email: String = ""
        }
    }
}