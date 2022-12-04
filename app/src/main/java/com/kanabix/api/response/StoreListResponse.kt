package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class StoreListResponse {
    @SerializedName("result")
    val result: StoreListResult = StoreListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class StoreListResult {
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("remainingItems")
    val remainingItems: Int = 0
    @SerializedName("count")
    val count: Int = 0
    @SerializedName("docs")
    val docs: ArrayList<StoreListDoc> = ArrayList()

}

class StoreListDoc {
    @SerializedName("store")
    val store: StoreListStore = StoreListStore()
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
    @SerializedName("keepStock")
    val keepStock: Boolean = false
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
    @SerializedName("deviceType")
    val deviceType: String = ""
    @SerializedName("deviceToken")
    val deviceToken: String = ""
    @SerializedName("password")
    val password: String = ""
    @SerializedName("govtDocument")
    val govtDocument: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("userUniqueId")
    val userUniqueId: String = ""
    @SerializedName("distance")
    val distance: Double = 0.0
    @SerializedName("no_of_products")
    val noOfProducts: Int = 0
    @SerializedName("no_of_likes")
    val noOfLikes: Int = 0
    @SerializedName("isliked")
    val isliked: Boolean = false

}

class StoreListStore{
    @SerializedName("storeName")
    val storeName: String = ""
    @SerializedName("storeAddress")
    val storeAddress: String = ""
    @SerializedName("merchantName")
    val merchantName: String = ""
    @SerializedName("noOfUniqueProducts")
    val noOfUniqueProducts: String = ""
    @SerializedName("keepStock")
    val keepStock: Boolean = false
    @SerializedName("storeImage")
    val storeImage: String = ""
}