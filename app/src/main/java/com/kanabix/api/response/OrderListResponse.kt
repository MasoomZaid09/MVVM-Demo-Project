package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class OrderListResponse {
    @SerializedName("result")
    val result: OrderListResult = OrderListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class OrderListResult {
    @SerializedName("docs")
    val docs: ArrayList<OrderListDoc> = ArrayList()
    @SerializedName("total")
    val total: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("pages")
    val pages: Int = 0
}

class OrderListDoc {
    @SerializedName("taxPrice")
    val taxPrice: Int = 0
    @SerializedName("orderStatus")
    val orderStatus: String = ""
    @SerializedName("paymentStatus")
    val paymentStatus: String = ""
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
    @SerializedName("productDetails")
    val productDetails: ArrayList<OrderListProductDetail> = ArrayList()
    @SerializedName("userId")
    val userId: OrderListUserId = OrderListUserId()
    @SerializedName("orderId")
    val orderId: String = ""
    @SerializedName("deliveryDateAndTime")
    val deliveryDateAndTime : String? = null
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class OrderListProductDetail {
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productId")
    val productId: OrderListProductId = OrderListProductId()
    @SerializedName("quantityRequested")
    val quantityRequested: Int = 0
    @SerializedName("unitPrice")
    val unitPrice: Int = 0
    @SerializedName("refundAmount")
    val refundAmount: Int = 0
    @SerializedName("quantityGet")
    val quantityGet: Int = 0
    @SerializedName("pendingQauntity")
    val pendingQauntity: Int = 0

}

class OrderListProductId {
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
    val categoryId: OrderListCategoryId = OrderListCategoryId()
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("productReferenceId")
    val productReferenceId: String = ""
    @SerializedName("userId")
    val userId: OrderListUserId = OrderListUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}


class OrderListCategoryId {
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


class OrderListUserId {
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