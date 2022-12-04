package com.kanabix.api.response


import com.google.gson.annotations.SerializedName


class NotificationListResponse {
    @SerializedName("result")
    val result: NotificationListResult = NotificationListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class NotificationListResult {
    @SerializedName("docs")
    val docs: List<NotificationListDoc> = listOf()
    @SerializedName("total")
    val total: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("pages")
    val pages: Int = 0
    @SerializedName("notificationCount")
    val notificationCount: Int = 0
}

class NotificationListDoc {
    @SerializedName("isRead")
    val isRead: Boolean = false
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("userId")
    val userId: NotificationListUserId = NotificationListUserId()
    @SerializedName("subject")
    val subject: String = ""
    @SerializedName("body")
    val body: String = ""
    @SerializedName("notifyType")
    val notifyType: String = ""
    @SerializedName("orderId")
    val orderId: NotificationListOrderId = NotificationListOrderId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("dealId")
    val dealId: NotificationListDealId = NotificationListDealId()

}

class NotificationListUserId {
    @SerializedName("location")
    val location: NotificationListLocation = NotificationListLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: NotificationListBusinessBankingDetails = NotificationListBusinessBankingDetails()
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

class NotificationListOrderId {
    @SerializedName("taxPrice")
    val taxPrice: Int = 0
    @SerializedName("orderStatus")
    val orderStatus: String = ""
    @SerializedName("paymentStatus")
    val paymentStatus: String = ""
//    @SerializedName("trackingArray")
//    val trackingArray: List<NotificationListAny> = listOf()
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
    val shippingFixedAddress: NotificationListShippingFixedAddress = NotificationListShippingFixedAddress()
    @SerializedName("productDetails")
    val productDetails: List<NotificationListProductDetail> = listOf()
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

class NotificationListDealId {
    @SerializedName("dealImage")
    val dealImage: List<String> = listOf()
    @SerializedName("discount")
    val discount: Int = 0
    @SerializedName("thumbnail")
    val thumbnail: String = ""
    @SerializedName("expired")
    val expired: Boolean = false
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("dealName")
    val dealName: String = ""
    @SerializedName("dealPrice")
    val dealPrice: Number = 0
    @SerializedName("description")
    val description: String = ""
    @SerializedName("productId")
    val productId: String = ""
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("dealStartTime")
    val dealStartTime: String = ""
    @SerializedName("dealEndTime")
    val dealEndTime: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

class NotificationListLocation {
    @SerializedName("type")
    val type: String = ""
    @SerializedName("coordinates")
    val coordinates: List<Double> = listOf()
}

class NotificationListBusinessBankingDetails {
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

class NotificationListShippingFixedAddress {
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

class NotificationListProductDetail {
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
