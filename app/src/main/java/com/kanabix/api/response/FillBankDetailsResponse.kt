package com.kanabix.api.response

import com.google.gson.annotations.SerializedName

class FillBankDetailsResponse {
    @SerializedName("result") val result : FillBankDetailsResult= FillBankDetailsResult()
    @SerializedName("responseMessage") val responseMessage : String=""
    @SerializedName("responseCode") val responseCode : Int=0
}
class FillBankDetailsResult {


    @SerializedName("businessBankingDetails")
    val businessBankingDetails: BusinessBankingDetails = BusinessBankingDetails()

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
    val zipCode: Int = 0

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

    @SerializedName("ownerFirstName")
    val ownerFirstName: String = ""

    @SerializedName("ownerLastName")
    val ownerLastName: String = ""

    @SerializedName("ownerEmail")
    val ownerEmail: String = ""

    @SerializedName("noOfUniqueProducts")
    val noOfUniqueProducts: Int = 0
}

class BusinessBankingDetails {

    @SerializedName("bankName")
    val bankName: String=""
    @SerializedName("accountHolderName")
    val accountHolderName: String=""
    @SerializedName("bank_IFSC")
    val bank_IFSC: String=""
    @SerializedName("accountType")
    val accountType: String=""
    @SerializedName("accountNumber")
    val accountNumber: String=""
}