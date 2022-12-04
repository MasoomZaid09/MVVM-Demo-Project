package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ResendOrderOtpResponse {
    @SerializedName("result")
    val result: ResendOrderOtpResult = ResendOrderOtpResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class ResendOrderOtpResult {
//    @SerializedName("taxPrice")
//    val taxPrice: Int = 0
//    @SerializedName("orderStatus")
//    val orderStatus: String = ""
//    @SerializedName("paymentStatus")
//    val paymentStatus: String = ""
//    @SerializedName("trackingArray")
//    val trackingArray: List<ResendOrderOtpTrackingArray> = listOf()
//    @SerializedName("deliveryPartnerStatus")
//    val deliveryPartnerStatus: String = ""
//    @SerializedName("status")
//    val status: String = ""
//    @SerializedName("_id")
//    val id: String = ""
//    @SerializedName("actualPrice")
//    val actualPrice: Int = 0
//    @SerializedName("dealsDiscount")
//    val dealsDiscount: Int = 0
//    @SerializedName("deliveryFee")
//    val deliveryFee: Int = 0
//    @SerializedName("merchantId")
//    val merchantId: String = ""
//    @SerializedName("orderPrice")
//    val orderPrice: Int = 0
//    @SerializedName("shippingFixedAddress")
//    val shippingFixedAddress: ResendOrderOtpShippingFixedAddress = ResendOrderOtpShippingFixedAddress()
//    @SerializedName("productDetails")
//    val productDetails: List<ResendOrderOtpProductDetail> = listOf()
//    @SerializedName("userId")
//    val userId: String = ""
//    @SerializedName("orderId")
//    val orderId: String = ""
//    @SerializedName("deliveryPartnerId")
//    val deliveryPartnerId: String = ""
//    @SerializedName("createdAt")
//    val createdAt: String = ""
//    @SerializedName("updatedAt")
//    val updatedAt: String = ""
//    @SerializedName("__v")
//    val v: Int = 0
//    @SerializedName("deliveryDateAndTime")
//    val deliveryDateAndTime: String = ""
//    @SerializedName("pickUpDate")
//    val pickUpDate: String = ""
//    @SerializedName("reason")
//    val reason: String = ""
//    @SerializedName("orderOtp")
//    val orderOtp: Int = 0
//    @SerializedName("orderOtpExpireTime")
//    val orderOtpExpireTime: Long = 0
//    @SerializedName("orderOtpVerification")
//    val orderOtpVerification: Boolean = false
//    class ResendOrderOtpTrackingArray {
//        @SerializedName("createdAt")
//        val createdAt: String = ""
//        @SerializedName("msg")
//        val msg: String = ""
//        @SerializedName("statusOfTracking")
//        val statusOfTracking: String = ""
//        @SerializedName("status")
//        val status: String = ""
//        @SerializedName("_id")
//        val id: String = ""
//        @SerializedName("orderId")
//        val orderId: String = ""
//        @SerializedName("__v")
//        val v: Int = 0
//    }
//
//    class ResendOrderOtpShippingFixedAddress {
//        @SerializedName("address")
//        val address: String = ""
//        @SerializedName("city")
//        val city: String = ""
//        @SerializedName("country")
//        val country: String = ""
//        @SerializedName("countryCode")
//        val countryCode: String = ""
//        @SerializedName("createdAt")
//        val createdAt: String = ""
//        @SerializedName("_id")
//        val id: String = ""
//        @SerializedName("isPrimary")
//        val isPrimary: Boolean = false
//        @SerializedName("landmark")
//        val landmark: String = ""
//        @SerializedName("mobileNumber")
//        val mobileNumber: String = ""
//        @SerializedName("name")
//        val name: String = ""
//        @SerializedName("state")
//        val state: String = ""
//        @SerializedName("status")
//        val status: String = ""
//        @SerializedName("updatedAt")
//        val updatedAt: String = ""
//        @SerializedName("userId")
//        val userId: String = ""
//        @SerializedName("__v")
//        val v: Int = 0
//        @SerializedName("zipCode")
//        val zipCode: String = ""
//    }
//
//    class ResendOrderOtpProductDetail {
//        @SerializedName("_id")
//        val id: String = ""
//        @SerializedName("productId")
//        val productId: String = ""
//        @SerializedName("quantityRequested")
//        val quantityRequested: Int = 0
//        @SerializedName("unitPrice")
//        val unitPrice: Int = 0
//        @SerializedName("totalPrice")
//        val totalPrice: Int = 0
//        @SerializedName("refundAmount")
//        val refundAmount: Int = 0
//        @SerializedName("quantityGet")
//        val quantityGet: Int = 0
//        @SerializedName("pendingQauntity")
//        val pendingQauntity: Int = 0
//    }
}