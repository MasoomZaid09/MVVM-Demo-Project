package com.kanabix.api.response


import com.google.gson.annotations.SerializedName


class CartListResponse {
    @SerializedName("result")
    val result: List<CartListResult> = listOf()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
    class CartListResult {


        @SerializedName("buyStatus")
        val buyStatus: String = ""
        @SerializedName("status")
        val status: String = ""
        @SerializedName("_id")
        val id: String = ""
        @SerializedName("merchantId")
        val merchantId: String = ""
        @SerializedName("productId")
        val productId: CartListProductId = CartListProductId()
        @SerializedName("quantity")
        val quantity: Int = 0
        @SerializedName("totalPrice")
        val totalPrice: Number = 0
        @SerializedName("dealAmount")
        val dealAmount: Number = 0
        @SerializedName("addType")
        val addType: String = ""
        @SerializedName("userId")
        val userId: CartListUserId = CartListUserId()
        @SerializedName("createdAt")
        val createdAt: String = ""
        @SerializedName("updatedAt")
        val updatedAt: String = ""
        @SerializedName("__v")
        val v: Int = 0

        var myFlag: Boolean = false

        class CartListProductId {
            @SerializedName("location")
            val location: CartListLocation = CartListLocation()
            @SerializedName("productImage")
            val productImage: List<String> = listOf()
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
            val price: Number = 0
            @SerializedName("description")
            val description: String = ""
            @SerializedName("categoryId")
            val categoryId: CartListCategoryId = CartListCategoryId()
            @SerializedName("quantity")
            val quantity: String = ""
            @SerializedName("productReferenceId")
            val productReferenceId: String = ""
            @SerializedName("userId")
            val userId: String = ""
            @SerializedName("createdAt")
            val createdAt: String = ""
            @SerializedName("updatedAt")
            val updatedAt: String = ""
            @SerializedName("__v")
            val v: Int = 0
            class CartListLocation {
                @SerializedName("type")
                val type: String = ""
                @SerializedName("coordinates")
                val coordinates: List<Double> = listOf()
            }

            class CartListCategoryId {
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
        }

        class CartListUserId {
            @SerializedName("location")
            val location: CartListLocation = CartListLocation()
            @SerializedName("businessBankingDetails")
            val businessBankingDetails: CartListBusinessBankingDetails = CartListBusinessBankingDetails()
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
            class CartListLocation {
                @SerializedName("type")
                val type: String = ""
                @SerializedName("coordinates")
                val coordinates: List<Double> = listOf()
            }

            class CartListBusinessBankingDetails {
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
        }
    }
}