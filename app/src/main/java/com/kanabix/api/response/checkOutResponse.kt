package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class checkOutResponse {
    @SerializedName("result")
    val result: checkOutResult = checkOutResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0


}

class checkOutResult {
    @SerializedName("transactionStatus")
    val transactionStatus: String = ""
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("transactionDetails")
    val transactionDetails: checkOutTransactionDetails = checkOutTransactionDetails()
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("orderId")
    val orderId: String = ""
    @SerializedName("orderAmount")
    val orderAmount: Int = 0
    @SerializedName("shippingAddress")
    val shippingAddress: String = ""
    @SerializedName("currencyCode")
    val currencyCode: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class checkOutTransactionDetails {
    @SerializedName("id")
    val id: String = ""
    @SerializedName("entity")
    val entity: String = ""
    @SerializedName("amount")
    val amount: Int = 0
    @SerializedName("amount_paid")
    val amountPaid: Int = 0
    @SerializedName("amount_due")
    val amountDue: Int = 0
    @SerializedName("currency")
    val currency: String = ""
    @SerializedName("receipt")
    val receipt: String = ""
//    @SerializedName("offer_id")
//    val offerId: checkOutAny = checkOutAny()
    @SerializedName("status")
    val status: String = ""
    @SerializedName("attempts")
    val attempts: Int = 0
//    @SerializedName("notes")
//    val notes: List<checkOutAny> = listOf()
    @SerializedName("created_at")
    val createdAt: Int = 0
}