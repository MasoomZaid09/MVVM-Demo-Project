package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ProductListResponse {
    @SerializedName("result")
    val result: ProductListResult = ProductListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class ProductListResult {
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("remainingItems")
    val remainingItems: Int = 0
    @SerializedName("count")
    val count: Int = 0
    @SerializedName("docs")
    val docs: ArrayList<ProductListDoc> = ArrayList()

}
class ProductListDoc {
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
    val categoryId: ProductListCategoryId = ProductListCategoryId()
    @SerializedName("price")
    val price: Int = 0
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("productReferenceId")
    val productReferenceId: String = ""
    @SerializedName("userId")
    val userId: ProductListUserId = ProductListUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("distance")
    val distance: Double = 0.0
    @SerializedName("isliked")
    val isliked: Boolean = false


}

class ProductListCategoryId {

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
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

class ProductListUserId {

    @SerializedName("store")
    val store: ProductListStore = ProductListStore()
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

}

class ProductListStore {
    @SerializedName("storeName")
    val storeName: String = ""
    @SerializedName("storeAddress")
    val storeAddress: String = ""
    @SerializedName("merchantName")
    val merchantName: String = ""
    @SerializedName("noOfUniqueProducts")
    val noOfUniqueProducts: String = ""
    @SerializedName("storeImage")
    val storeImage: String = ""
}




//class ProductListBusinessDocumentUpload {
//    @SerializedName("cirtificationOfIncorporation")
//    val cirtificationOfIncorporation: ProductListCirtificationOfIncorporation = ProductListCirtificationOfIncorporation()
//    @SerializedName("VatRegConfirmationDocs")
//    val vatRegConfirmationDocs: ProductListVatRegConfirmationDocs = ProductListVatRegConfirmationDocs()
//    @SerializedName("directConsentForm")
//    val directConsentForm: ProductListDirectConsentForm = ProductListDirectConsentForm()
//    @SerializedName("directorId")
//    val directorId: ProductListDirectorId = ProductListDirectorId()
//    @SerializedName("bankConfirmationLetter")
//    val bankConfirmationLetter: ProductListBankConfirmationLetter = ProductListBankConfirmationLetter()
//    class ProductListCirtificationOfIncorporation {
//        @SerializedName("frontImage")
//        val frontImage: String = ""
//        @SerializedName("backImage")
//        val backImage: String = ""
//    }
//
//    class ProductListVatRegConfirmationDocs {
//        @SerializedName("frontImage")
//        val frontImage: String = ""
//        @SerializedName("backImage")
//        val backImage: String = ""
//    }
//
//    class ProductListDirectConsentForm {
//        @SerializedName("frontImage")
//        val frontImage: String = ""
//        @SerializedName("backImage")
//        val backImage: String = ""
//    }
//
//    class ProductListDirectorId {
//        @SerializedName("frontImage")
//        val frontImage: String = ""
//        @SerializedName("backImage")
//        val backImage: String = ""
//    }
//
//    class ProductListBankConfirmationLetter {
//        @SerializedName("frontImage")
//        val frontImage: String = ""
//        @SerializedName("backImage")
//        val backImage: String = ""
//    }
//}