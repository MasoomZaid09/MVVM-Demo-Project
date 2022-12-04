package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ViewOrderResponse {
    @SerializedName("result")
    val result: ViewOrderResult = ViewOrderResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class ViewOrderResult {
    @SerializedName("taxPrice")
    val taxPrice: Int = 0
    @SerializedName("orderStatus")
    val orderStatus: String = ""
    @SerializedName("paymentStatus")
    val paymentStatus: String = ""
    @SerializedName("trackingArray")
    val trackingArray: List<ViewOrderTrackingArray> = listOf()
    @SerializedName("deliveryPartnerStatus")
    val deliveryPartnerStatus: String = ""
    @SerializedName("cartId")
    val cartId: List<String> = listOf()
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("actualPrice")
    val actualPrice: Int = 0
    @SerializedName("dealsDiscount")
    val dealsDiscount: Int = 0
    @SerializedName("deliveryFee")
    val deliveryFee: Int = 0
    @SerializedName("merchantId")
    val merchantId: String = ""
    @SerializedName("orderPrice")
    val orderPrice: Int = 0
    @SerializedName("productDetails")
    val productDetails: ArrayList<ViewOrderProductDetail> = ArrayList()
    @SerializedName("userId")
    val userId: ViewOrderUserId = ViewOrderUserId()
    @SerializedName("orderId")
    val orderId: String = ""
//    @SerializedName("deliveryPartnerId")
//    val deliveryPartnerId: ViewOrderDeliveryPartnerId = ViewOrderDeliveryPartnerId()
//    @SerializedName("deliveryDateAndTime")
//    val deliveryDateAndTime: ViewOrderAny = ViewOrderAny()
    @SerializedName("deliveryDateAndTime")
    val deliveryDateAndTime: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("shippingFixedAddress")
    val shippingFixedAddress: ViewOrderShippingFixedAddress = ViewOrderShippingFixedAddress()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
//    @SerializedName("pickUpDate")
//    val pickUpDate: ViewOrderAny = ViewOrderAny()
    @SerializedName("pickUpDate")
    val pickUpDate: String = ""
    @SerializedName("reason")
    val reason: String = ""
    @SerializedName("declinedDate")
    val declinedDate: String = ""

}

class ViewOrderTrackingArray {
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("msg")
    val msg: String = ""
    @SerializedName("statusOfTracking")
    val statusOfTracking: String = ""
}

class ViewOrderProductDetail {
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productId")
    val productId: ViewOrderProductId = ViewOrderProductId()
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

class ViewOrderProductId {
    @SerializedName("location")
    val location: ViewOrderLocation = ViewOrderLocation()
    @SerializedName("productImage")
    val productImage: ArrayList<String> = ArrayList()
    @SerializedName("discount")
    val discount: Int = 0
    @SerializedName("thumbnail")
    val thumbnail: String = ""
    @SerializedName("expectedDeliveryDays")
    val expectedDeliveryDays: String = ""
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productName")
    val productName: String = ""
    @SerializedName("price")
    val price: Int = 0
    @SerializedName("description")
    val description: String = ""
    @SerializedName("categoryId")
    val categoryId: ViewOrderCategoryId = ViewOrderCategoryId()
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("productReferenceId")
    val productReferenceId: String = ""
    @SerializedName("userId")
    val userId: ViewOrderUserId = ViewOrderUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class ViewOrderLocation {
    @SerializedName("type")
    val type: String = ""
    @SerializedName("coordinates")
    val coordinates: List<Double> = listOf()
}

class ViewOrderCategoryId {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("categoryName")
    val categoryName: String = ""
    @SerializedName("categoryImage")
    val categoryImage: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("thumbnail")
    val thumbnail: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

class ViewOrderUserId {
    @SerializedName("location")
    val location: ViewOrderLocation = ViewOrderLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: ViewOrderBusinessBankingDetails = ViewOrderBusinessBankingDetails()
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
    @SerializedName("userType")
    val userType: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("password")
    val password: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("country")
    val country: String = ""
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

class ViewOrderBusinessBankingDetails {
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

//class ViewOrderDeliveryPartnerId {
//    @SerializedName("location")
//    val location: ViewOrderLocation = ViewOrderLocation()
//    @SerializedName("businessBankingDetails")
//    val businessBankingDetails: ViewOrderBusinessBankingDetails = ViewOrderBusinessBankingDetails()
//    @SerializedName("address")
//    val address: String = ""
//    @SerializedName("otpVerification")
//    val otpVerification: Boolean = false
//    @SerializedName("userVerification")
//    val userVerification: Boolean = false
//    @SerializedName("profilePic")
//    val profilePic: String = ""
//    @SerializedName("userRequestStatus")
//    val userRequestStatus: String = ""
//    @SerializedName("zipCode")
//    val zipCode: String = ""
//    @SerializedName("DOB")
//    val dOB: String = ""
//    @SerializedName("completeProfile")
//    val completeProfile: Boolean = false
//    @SerializedName("flag")
//    val flag: Int = 0
//    @SerializedName("placeOrderCount")
//    val placeOrderCount: Int = 0
//    @SerializedName("serviceOrderCount")
//    val serviceOrderCount: Int = 0
//    @SerializedName("receiveOrderCount")
//    val receiveOrderCount: Int = 0
//    @SerializedName("status")
//    val status: String = ""
//    @SerializedName("thumbnail")
//    val thumbnail: String = ""
//    @SerializedName("locationSet")
//    val locationSet: Boolean = false
//    @SerializedName("_id")
//    val id: String = ""
//    @SerializedName("city")
//    val city: String = ""
//    @SerializedName("country")
//    val country: String = ""
//    @SerializedName("countryCode")
//    val countryCode: String = ""
//    @SerializedName("countryIsoCode")
//    val countryIsoCode: String = ""
//    @SerializedName("email")
//    val email: String = ""
//    @SerializedName("fileName")
//    val fileName: String = ""
//    @SerializedName("govtDocument")
//    val govtDocument: String = ""
//    @SerializedName("mobileNumber")
//    val mobileNumber: String = ""
//    @SerializedName("name")
//    val name: String = ""
//    @SerializedName("password")
//    val password: String = ""
//    @SerializedName("state")
//    val state: String = ""
//    @SerializedName("stateIsoCode")
//    val stateIsoCode: String = ""
//    @SerializedName("userType")
//    val userType: String = ""
//    @SerializedName("createdAt")
//    val createdAt: String = ""
//    @SerializedName("updatedAt")
//    val updatedAt: String = ""
//    @SerializedName("__v")
//    val v: Int = 0
//    @SerializedName("userUniqueId")
//    val userUniqueId: String = ""
//    @SerializedName("otp")
//    val otp: String = ""
//    @SerializedName("otpExpireTime")
//    val otpExpireTime: Long = 0
////    @SerializedName("deviceToken")
////    val deviceToken: ViewOrderAny = ViewOrderAny()
////    @SerializedName("deviceType")
////    val deviceType: ViewOrderAny = ViewOrderAny()
//
//}

class ViewOrderShippingFixedAddress {
    @SerializedName("location")
    val location: ViewOrderLocation = ViewOrderLocation()
    @SerializedName("isPrimary")
    val isPrimary: Boolean = false
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("address")
    val address: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("zipCode")
    val zipCode: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    class ViewOrderLocation {
        @SerializedName("type")
        val type: String = ""
        @SerializedName("coordinates")
        val coordinates: List<Double> = listOf()
    }
}