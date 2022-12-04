package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class EditBankDetailsRequest {
    @SerializedName("businessBankingDetails")
    var businessBankingDetails: EditBankDetailsBusinessBankingDetails = EditBankDetailsBusinessBankingDetails()
}
class EditBankDetailsBusinessBankingDetails {
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