package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class WishListResponse {
    @SerializedName("result")
    val result: WishListResult = WishListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class WishListResult {
    @SerializedName("docs")
    val docs: ArrayList<WishListDoc> = ArrayList()

}

class WishListDoc {
    @SerializedName("isLike")
    val isLike: Boolean = false
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productId")
    val productId: WishListProductId = WishListProductId()
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class WishListProductId {
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
    @SerializedName("description")
    val description: String = ""
    @SerializedName("categoryId")
    val categoryId: String = ""
    @SerializedName("price")
    val price: Int = 0
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