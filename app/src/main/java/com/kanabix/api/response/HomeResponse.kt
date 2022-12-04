package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class HomeResponse {
    @SerializedName("result")
    val result: HomeResult = HomeResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}
class HomeResult {
    @SerializedName("categoryDetails")
    val categoryDetails: ArrayList<HomeCategoryDetail> = ArrayList()
    @SerializedName("bannerDetails")
    val bannerDetails: ArrayList<HomeBannerDetail> = ArrayList()
    @SerializedName("productDetails")
    val productDetails: ArrayList<ProductListDoc> = ArrayList()
    @SerializedName("dealDetails")
    val dealDetails: ArrayList<HomeDealDetail> = ArrayList()

}

class HomeCategoryDetail {
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

class HomeBannerDetail {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

//class HomeProductDetail {
//    @SerializedName("productImage")
//    val productImage: ArrayList<String> = ArrayList()
//    @SerializedName("discount")
//    val discount: Int = 0
//    @SerializedName("thumbnail")
//    val thumbnail: String = ""
//    @SerializedName("expectedDeliveryDays")
//    val expectedDeliveryDays: String = ""
//    @SerializedName("status")
//    val status: String = ""
//    @SerializedName("_id")
//    val id: String = ""
//    @SerializedName("productName")
//    val productName: String = ""
//    @SerializedName("price")
//    val price: Int = 0
//    @SerializedName("description")
//    val description: String = ""
//    @SerializedName("quantity")
//    val quantity: String = ""
//    @SerializedName("productReferenceId")
//    val productReferenceId: String = ""
//    @SerializedName("createdAt")
//    val createdAt: String = ""
//    @SerializedName("updatedAt")
//    val updatedAt: String = ""
//    @SerializedName("__v")
//    val v: Int = 0
//
//}


class HomeDealDetail {
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
    val dealPrice: Int = 0
    @SerializedName("description")
    val description: String = ""
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
}
