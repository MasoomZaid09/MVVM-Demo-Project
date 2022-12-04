package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class BankDetailsRequest {
    @SerializedName("userId")
    var userId: String = ""
    @SerializedName("flag")
    var flag: Int = 0
    @SerializedName("businessBankingDetails")
    var businessBankingDetails: BankDetailsBusinessBankingDetails = BankDetailsBusinessBankingDetails()
}

class BankDetailsBusinessBankingDetails {
    @SerializedName("bankName")
    var bankName: String = ""
    @SerializedName("accountHolderName")
    var accountHolderName: String = ""
    @SerializedName("bank_IFSC")
    var bankIFSC: String = ""
    @SerializedName("accountType")
    var accountType: String = ""
    @SerializedName("accountNumber")
    var accountNumber: String = ""
}