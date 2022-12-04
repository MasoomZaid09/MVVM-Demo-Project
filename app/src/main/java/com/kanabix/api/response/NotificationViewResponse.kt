package com.kanabix.api.response


import com.google.gson.annotations.SerializedName


class NotificationViewResponse {
    @SerializedName("result")
    val result: NotificationViewResult = NotificationViewResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class NotificationViewResult {
    @SerializedName("isRead")
    val isRead: Boolean = false
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("userId")
    val userId: NotificationViewUserId = NotificationViewUserId()
    @SerializedName("subject")
    val subject: String = ""
    @SerializedName("body")
    val body: String = ""
    @SerializedName("notifyType")
    val notifyType: String = ""
    @SerializedName("orderId")
    val orderId: NotificationViewOrderId = NotificationViewOrderId()
    @SerializedName("dealId")
    val dealId: NotificationViewDealId = NotificationViewDealId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class NotificationViewDealId {
    @SerializedName("_id")
    val _id: String = ""
}

class NotificationViewUserId {
    @SerializedName("location")
    val location: NotificationViewLocation = NotificationViewLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: NotificationViewBusinessBankingDetails = NotificationViewBusinessBankingDetails()
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
    @SerializedName("ownerFirstName")
    val ownerFirstName: String = ""
    @SerializedName("ownerLastName")
    val ownerLastName: String = ""
    @SerializedName("ownerEmail")
    val ownerEmail: String = ""
    @SerializedName("noOfUniqueProducts")
    val noOfUniqueProducts: Int = 0
    @SerializedName("listOfBrandOrProducts")
    val listOfBrandOrProducts: List<String> = listOf()
    @SerializedName("keepStock")
    val keepStock: Boolean = false
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

}

class NotificationViewOrderId {
    @SerializedName("taxPrice")
    val taxPrice: Int = 0
    @SerializedName("orderStatus")
    val orderStatus: String = ""
    @SerializedName("paymentStatus")
    val paymentStatus: String = ""
//    @SerializedName("trackingArray")
//    val trackingArray: List<NotificationViewAny> = listOf()
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("actualPrice")
    val actualPrice: Int = 0
    @SerializedName("dealsDiscount")
    val dealsDiscount: Int = 0
    @SerializedName("orderPrice")
    val orderPrice: Int = 0
    @SerializedName("shippingFixedAddress")
    val shippingFixedAddress: NotificationViewShippingFixedAddress = NotificationViewShippingFixedAddress()
    @SerializedName("productDetails")
    val productDetails: List<NotificationViewProductDetail> = listOf()
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("orderId")
    val orderId: String = ""
    @SerializedName("deliveryDateAndTime")
    val deliveryDateAndTime: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class NotificationViewLocation {
    @SerializedName("type")
    val type: String = ""
    @SerializedName("coordinates")
    val coordinates: List<Double> = listOf()
}

class NotificationViewBusinessBankingDetails {
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

class NotificationViewShippingFixedAddress {
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

class NotificationViewProductDetail {
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productId")
    val productId: String = ""
    @SerializedName("quantityRequested")
    val quantityRequested: Int = 0
    @SerializedName("unitPrice")
    val unitPrice: Int = 0
    @SerializedName("totalPrice")
    val totalPrice: Int = 0
    @SerializedName("refundAmount")
    val refundAmount: Int = 0
    @SerializedName("quantityGet")
    val quantityGet: Int = 0
    @SerializedName("pendingQauntity")
    val pendingQauntity: Int = 0
}