package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ViewBankDetailsResponse {
    @SerializedName("result")
    val result: ViewBankDetailsResult = ViewBankDetailsResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class ViewBankDetailsResult {
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: ViewBankDetailsBusinessBankingDetails = ViewBankDetailsBusinessBankingDetails()
    @SerializedName("address")
    val address: String = ""
    @SerializedName("otpVerification")
    val otpVerification: Boolean = false
    @SerializedName("userVerification")
    val userVerification: Boolean = false
    @SerializedName("profilePic")
    val profilePic: String = ""
    @SerializedName("userRequestStatus")
    val userRequestStatus: String = ""
    @SerializedName("zipCode")
    val zipCode: String = ""
    @SerializedName("DOB")
    val dOB: String = ""
    @SerializedName("completeProfile")
    val completeProfile: Boolean = false
    @SerializedName("flag")
    val flag: Int = 0
    @SerializedName("placeOrderCount")
    val placeOrderCount: Int = 0
    @SerializedName("serviceOrderCount")
    val serviceOrderCount: Int = 0
    @SerializedName("receiveOrderCount")
    val receiveOrderCount: Int = 0
    @SerializedName("status")
    val status: String = ""
    @SerializedName("thumbnail")
    val thumbnail: String = ""
    @SerializedName("locationSet")
    val locationSet: Boolean = false
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("password")
    val password: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("userType")
    val userType: String = ""
    @SerializedName("otp")
    val otp: String = ""
    @SerializedName("otpExpireTime")
    val otpExpireTime: Long = 0
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("isReset")
    val isReset: Boolean = false


}

class ViewBankDetailsBusinessBankingDetails {
    @SerializedName("bankName")
    val bankName: String = ""
    @SerializedName("accountHolderName")
    val accountHolderName: String = ""
    @SerializedName("bank_IFSC")
    val bankIFSC: String = ""
    @SerializedName("accountType")
    val accountType: String = ""
    @SerializedName("accountNumber")
    val accountNumber: String = ""
}