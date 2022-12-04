package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class ViewProductResponse {
    @SerializedName("result")
    val result: ViewProductResult = ViewProductResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}
class ViewProductResult {
    @SerializedName("location")
    val location: ViewProductLocation = ViewProductLocation()
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
    val categoryId: ViewProductCategoryId = ViewProductCategoryId()
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("productReferenceId")
    val productReferenceId: String = ""
    @SerializedName("userId")
    val userId: ViewProductUserId = ViewProductUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("isstoreLiked")
    val isstoreLiked: Boolean = false
    @SerializedName("no_of_likes")
    val noOfLikes: Int = 0
    @SerializedName("no_of_products")
    val noOfProducts: Int = 0
    @SerializedName("isLike")
    var isLike: Boolean = false
    @SerializedName("isAddedToCart")
    var isAddedToCart: Boolean = false

}

class ViewProductLocation {
    @SerializedName("type")
    val type: String = ""
    @SerializedName("coordinates")
    val coordinates: List<Double> = listOf()
}

class ViewProductCategoryId {
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

class ViewProductUserId {
    @SerializedName("location")
    val location: ViewProductLocation = ViewProductLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: ViewProductBusinessBankingDetails = ViewProductBusinessBankingDetails()
    @SerializedName("store")
    val store: ViewProductStore = ViewProductStore()
    @SerializedName("businessDocumentUpload")
    val businessDocumentUpload: ViewProductBusinessDocumentUpload = ViewProductBusinessDocumentUpload()
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
    @SerializedName("listOfBrandOrProducts")
    val listOfBrandOrProducts: List<String> = listOf()
    @SerializedName("keepStock")
    val keepStock: Boolean = false
    @SerializedName("deliveryPartnerId")
    val deliveryPartnerId: String = ""
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
    class ViewProductLocation {
        @SerializedName("type")
        val type: String = ""
        @SerializedName("coordinates")
        val coordinates: List<Double> = listOf()
    }

    class ViewProductBusinessBankingDetails {
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

    class ViewProductStore {
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

    class ViewProductBusinessDocumentUpload {
        @SerializedName("cirtificationOfIncorporation")
        val cirtificationOfIncorporation: ViewProductCirtificationOfIncorporation = ViewProductCirtificationOfIncorporation()
        @SerializedName("VatRegConfirmationDocs")
        val vatRegConfirmationDocs: ViewProductVatRegConfirmationDocs = ViewProductVatRegConfirmationDocs()
        @SerializedName("directConsentForm")
        val directConsentForm: ViewProductDirectConsentForm = ViewProductDirectConsentForm()
        @SerializedName("directorId")
        val directorId: ViewProductDirectorId = ViewProductDirectorId()
        @SerializedName("bankConfirmationLetter")
        val bankConfirmationLetter: ViewProductBankConfirmationLetter = ViewProductBankConfirmationLetter()
        class ViewProductCirtificationOfIncorporation {
            @SerializedName("frontImage")
            val frontImage: String = ""
            @SerializedName("backImage")
            val backImage: String = ""
        }

        class ViewProductVatRegConfirmationDocs {
            @SerializedName("frontImage")
            val frontImage: String = ""
            @SerializedName("backImage")
            val backImage: String = ""
        }

        class ViewProductDirectConsentForm {
            @SerializedName("frontImage")
            val frontImage: String = ""
            @SerializedName("backImage")
            val backImage: String = ""
        }

        class ViewProductDirectorId {
            @SerializedName("frontImage")
            val frontImage: String = ""
            @SerializedName("backImage")
            val backImage: String = ""
        }

        class ViewProductBankConfirmationLetter {
            @SerializedName("frontImage")
            val frontImage: String = ""
            @SerializedName("backImage")
            val backImage: String = ""
        }
    }
}