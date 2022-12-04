package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ViewDealResponse {
    @SerializedName("result")
    val result: ViewDealResult = ViewDealResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}
class ViewDealResult {
    @SerializedName("dealImage")
    val dealImage: ArrayList<String> = ArrayList()
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
    val productId: ViewDealProductId = ViewDealProductId()
    @SerializedName("userId")
    val userId: ViewDealUserId = ViewDealUserId()
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("dealStartTime")
    val dealStartTime: String = ""
    @SerializedName("dealEndTime")
    val dealEndTime: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("no_of_likes")
    val no_of_likes: Int = 0
    @SerializedName("no_of_products")
    val no_of_products: Int = 0
    @SerializedName("isAddedToCart")
    var isAddedToCart: Boolean = false
}

class ViewDealUserId {

    @SerializedName("store")
    var store: ViewDealStore? = ViewDealStore()
    @SerializedName("address")
    var address: String? = ""
    @SerializedName("otpVerification")
    var otpVerification: Boolean? = false
    @SerializedName("userVerification")
    var userVerification: Boolean? = false
    @SerializedName("profilePic")
    var profilePic: String? = ""
    @SerializedName("userRequestStatus")
    var userRequestStatus: String? = ""
    @SerializedName("zipCode")
    var zipCode: String? = ""
    @SerializedName("DOB")
    var DOB: String? = ""
    @SerializedName("completeProfile")
    var completeProfile: Boolean? = false
    @SerializedName("flag")
    var flag: Int? = 0
    @SerializedName("placeOrderCount")
    var placeOrderCount: Int? = 0
    @SerializedName("serviceOrderCount")
    var serviceOrderCount: Int? = 0
    @SerializedName("receiveOrderCount")
    var receiveOrderCount: Int? = 0
    @SerializedName("status")
    var status: String? = ""
    @SerializedName("thumbnail")
    var thumbnail: String? = ""
    @SerializedName("locationSet")
    var locationSet: Boolean? = false
    @SerializedName("_id")
    var Id: String? = ""
    @SerializedName("ownerFirstName")
    var ownerFirstName: String? = ""
    @SerializedName("ownerLastName")
    var ownerLastName: String? = ""
    @SerializedName("ownerEmail")
    var ownerEmail: String? = ""
    @SerializedName("noOfUniqueProducts")
    var noOfUniqueProducts: Int? = 0
//    @SerializedName("listOfBrandOrProducts")
//    var listOfBrandOrProducts: ArrayList<String> = ArrayList()
    @SerializedName("keepStock")
    var keepStock: Boolean? = false
    @SerializedName("userType")
    var userType: String? = ""
    @SerializedName("name")
    var name: String? = ""
    @SerializedName("countryCode")
    var countryCode: String? = ""
    @SerializedName("mobileNumber")
    var mobileNumber: String? = ""
    @SerializedName("email")
    var email: String? = ""
    @SerializedName("deviceType")
    var deviceType: String? = ""
    @SerializedName("deviceToken")
    var deviceToken: String? = ""
    @SerializedName("password")
    var password: String? = ""
    @SerializedName("govtDocument")
    var govtDocument: String? = ""
    @SerializedName("city")
    var city: String? = ""
    @SerializedName("state")
    var state: String? = ""
    @SerializedName("country")
    var country: String? = ""
    @SerializedName("createdAt")
    var createdAt: String? = ""
    @SerializedName("updatedAt")
    var updatedAt: String? = ""
    @SerializedName("__v")
    var _v: Int? = 0
    @SerializedName("userUniqueId")
    var userUniqueId: String? = ""
}

class ViewDealStore {

    @SerializedName("storeName")
    var storeName: String? = ""

    @SerializedName("storeAddress")
    var storeAddress: String? = ""

    @SerializedName("merchantName")
    var merchantName: String? = ""

    @SerializedName("noOfUniqueProducts")
    var noOfUniqueProducts: String? = ""

    @SerializedName("keepStock")
    var keepStock: Boolean? = false

    @SerializedName("storeImage")
    var storeImage: String? = ""

}

class ViewDealProductId {
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
    val categoryId: ViewDealCategoryId = ViewDealCategoryId()
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


}

class ViewDealCategoryId {
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