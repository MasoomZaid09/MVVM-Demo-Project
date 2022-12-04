package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class VerifyPaymentResponse {
    @SerializedName("result")
    val result: VerifyPaymentResult = VerifyPaymentResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class VerifyPaymentResult {
    @SerializedName("transactionStatus")
    val transactionStatus: String = ""
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("transactionDetails")
    val transactionDetails: VerifyPaymentTransactionDetails = VerifyPaymentTransactionDetails()
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("orderId")
    val orderId: String = ""
    @SerializedName("orderAmount")
    val orderAmount: Int = 0
    @SerializedName("currencyCode")
    val currencyCode: String = ""
    @SerializedName("shippingFixedAddress")
    val shippingFixedAddress: VerifyPaymentShippingFixedAddress = VerifyPaymentShippingFixedAddress()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("paidDetails")
    val paidDetails: VerifyPaymentPaidDetails = VerifyPaymentPaidDetails()

}

class VerifyPaymentTransactionDetails {
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
//    val offerId: VerifyPaymentAny = VerifyPaymentAny()
    @SerializedName("status")
    val status: String = ""
    @SerializedName("attempts")
    val attempts: Int = 0
//    @SerializedName("notes")
//    val notes: List<VerifyPaymentAny> = listOf()
    @SerializedName("created_at")
    val createdAt: Int = 0
}

class VerifyPaymentShippingFixedAddress {
    @SerializedName("address")
    val address: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("isPrimary")
    val isPrimary: Boolean = false
    @SerializedName("landmark")
    val landmark: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("status")
    val status: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("zipCode")
    val zipCode: String = ""
}

class VerifyPaymentPaidDetails {
    @SerializedName("razorpay_payment_id")
    val razorpayPaymentId: String = ""
    @SerializedName("razorpay_order_id")
    val razorpayOrderId: String = ""
    @SerializedName("razorpay_signature")
    val razorpaySignature: String = ""
}