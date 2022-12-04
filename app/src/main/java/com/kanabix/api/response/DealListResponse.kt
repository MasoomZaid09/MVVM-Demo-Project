package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class DealListResponse {
    @SerializedName("result")
    val result: DealListResult = DealListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class DealListResult {
    @SerializedName("docs")
    val docs: ArrayList<DealListDoc> = ArrayList()
}

class DealListDoc {
    @SerializedName("dealImage")
    val dealImage: ArrayList<String> = ArrayList()
    @SerializedName("discount")
    val discount: Double = 0.0
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
    val dealPrice: Double = 0.0
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("dealStartTime")
    val dealStartTime: String = ""
    @SerializedName("dealEndTime")
    val dealEndTime: String = ""
    @SerializedName("productId")
    val productId: DealListProductId = DealListProductId()
    @SerializedName("userId")
    val userId: DealListUserId = DealListUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("distance")
    val distance: Double = 0.0

}

class DealListProductId {
    @SerializedName("location")
    val location: DealListLocation = DealListLocation()
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
    @SerializedName("categoryId")
    val categoryId: DealListCategoryId = DealListCategoryId()
    @SerializedName("productName")
    val productName: String = ""
    @SerializedName("price")
    val price: Int = 0
    @SerializedName("description")
    val description: String = ""
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    class DealListLocation {
        @SerializedName("type")
        val type: String = ""
        @SerializedName("coordinates")
        val coordinates: List<Double> = listOf()
    }

    class DealListCategoryId {
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

class DealListUserId {
    @SerializedName("location")
    val location: DealListLocation = DealListLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: DealListBusinessBankingDetails = DealListBusinessBankingDetails()
    @SerializedName("store")
    val store: DealListStore = DealListStore()
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
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("email")
    val email: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("govtDocument")
    val govtDocument: String = ""
    @SerializedName("password")
    val password: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("userUniqueId")
    val userUniqueId: String = ""
    @SerializedName("deliveryPartnerId")
    val deliveryPartnerId: String = ""
    @SerializedName("updateDocumentStatus")
    val updateDocumentStatus: String = ""
    class DealListLocation {
        @SerializedName("type")
        val type: String = ""
        @SerializedName("coordinates")
        val coordinates: List<Double> = listOf()
    }

    class DealListBusinessBankingDetails {
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

    class DealListStore {
        @SerializedName("storeName")
        val storeName: String = ""
        @SerializedName("storeAddress")
        val storeAddress: String = ""
        @SerializedName("merchantName")
        val merchantName: String = ""
        @SerializedName("noOfUniqueProducts")
        val noOfUniqueProducts: String = ""
    }
}