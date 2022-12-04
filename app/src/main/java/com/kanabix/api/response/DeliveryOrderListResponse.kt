package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class DeliveryOrderListResponse {
    @SerializedName("result")
    val result: DeliveryOrderListResult = DeliveryOrderListResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class DeliveryOrderListResult {

    @SerializedName("docs")
    val docs: ArrayList<DeliveryOrderListDoc> = ArrayList()
    @SerializedName("total")
    val total: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("pages")
    val pages: Int = 0
}


class DeliveryOrderListDoc {
    @SerializedName("taxPrice")
    val taxPrice: Int = 0
    @SerializedName("orderStatus")
    val orderStatus: String = ""
    @SerializedName("paymentStatus")
    val paymentStatus: String = ""
    @SerializedName("deliveryPartnerStatus")
    val deliveryPartnerStatus: String = ""
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
    @SerializedName("shippingFixedAddress")
    val shippingFixedAddress: DeliveryOrderListShippingFixedAddress = DeliveryOrderListShippingFixedAddress()
    @SerializedName("productDetails")
    val productDetails: ArrayList<DeliveryOrderListProductDetail> = ArrayList()
    @SerializedName("userId")
    val userId: DeliveryOrderListUserId = DeliveryOrderListUserId()
    @SerializedName("orderId")
    val orderId: String = ""
//    @SerializedName("deliveryPartnerId")
//    val deliveryPartnerId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0

}

class DeliveryOrderListShippingFixedAddress {
    @SerializedName("address")
    val address: String = ""
    @SerializedName("city")
    val city: String = ""
    @SerializedName("country")
    val country: String = ""
    @SerializedName("countryCode")
    val countryCode: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("isPrimary")
    val isPrimary: Boolean = false
    @SerializedName("landmark")
    val landmark: String = ""
    @SerializedName("mobileNumber")
    val mobileNumber: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("state")
    val state: String = ""
    @SerializedName("status")
    val status: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("__v")
    val v: Int = 0
    @SerializedName("zipCode")
    val zipCode: String = ""
}

class DeliveryOrderListProductDetail {
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("productId")
    val productId: DeliveryOrderListProductId = DeliveryOrderListProductId()
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

class DeliveryOrderListProductId {

    @SerializedName("location")
    val location: DeliveryOrderListLocation = DeliveryOrderListLocation()
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
    val categoryId: DeliveryOrderListCategoryId = DeliveryOrderListCategoryId()
    @SerializedName("quantity")
    val quantity: String = ""
    @SerializedName("productReferenceId")
    val productReferenceId: String = ""
    @SerializedName("userId")
    val userId: DeliveryOrderListUserId = DeliveryOrderListUserId()
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

class DeliveryOrderListLocation {
    @SerializedName("type")
    val type: String = ""
    @SerializedName("coordinates")
    val coordinates: List<Double> = listOf()
}

class DeliveryOrderListCategoryId {
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

class DeliveryOrderListUserId {
    @SerializedName("location")
    val location: DeliveryOrderListLocation = DeliveryOrderListLocation()
    @SerializedName("businessBankingDetails")
    val businessBankingDetails: DeliveryOrderListBusinessBankingDetails = DeliveryOrderListBusinessBankingDetails()
    @SerializedName("store")
    val store: DeliveryOrderListStore = DeliveryOrderListStore()
    @SerializedName("businessDocumentUpload")
    val businessDocumentUpload: DeliveryOrderListBusinessDocumentUpload = DeliveryOrderListBusinessDocumentUpload()
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

}

class DeliveryOrderListBusinessBankingDetails {
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

class DeliveryOrderListStore {
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

class DeliveryOrderListBusinessDocumentUpload {
    @SerializedName("cirtificationOfIncorporation")
    val cirtificationOfIncorporation: DeliveryOrderListCirtificationOfIncorporation = DeliveryOrderListCirtificationOfIncorporation()
    @SerializedName("VatRegConfirmationDocs")
    val vatRegConfirmationDocs: DeliveryOrderListVatRegConfirmationDocs = DeliveryOrderListVatRegConfirmationDocs()
    @SerializedName("directConsentForm")
    val directConsentForm: DeliveryOrderListDirectConsentForm = DeliveryOrderListDirectConsentForm()
    @SerializedName("directorId")
    val directorId: DeliveryOrderListDirectorId = DeliveryOrderListDirectorId()
    @SerializedName("bankConfirmationLetter")
    val bankConfirmationLetter: DeliveryOrderListBankConfirmationLetter = DeliveryOrderListBankConfirmationLetter()
    class DeliveryOrderListCirtificationOfIncorporation {
        @SerializedName("frontImage")
        val frontImage: String = ""
        @SerializedName("backImage")
        val backImage: String = ""
    }

    class DeliveryOrderListVatRegConfirmationDocs {
        @SerializedName("frontImage")
        val frontImage: String = ""
        @SerializedName("backImage")
        val backImage: String = ""
    }

    class DeliveryOrderListDirectConsentForm {
        @SerializedName("frontImage")
        val frontImage: String = ""
        @SerializedName("backImage")
        val backImage: String = ""
    }

    class DeliveryOrderListDirectorId {
        @SerializedName("frontImage")
        val frontImage: String = ""
        @SerializedName("backImage")
        val backImage: String = ""
    }

    class DeliveryOrderListBankConfirmationLetter {
        @SerializedName("frontImage")
        val frontImage: String = ""
        @SerializedName("backImage")
        val backImage: String = ""
    }
}

//class DeliveryOrderListUserId {
//    @SerializedName("location")
//    val location: DeliveryOrderListLocation = DeliveryOrderListLocation()
//    @SerializedName("businessBankingDetails")
//    val businessBankingDetails: DeliveryOrderListBusinessBankingDetails = DeliveryOrderListBusinessBankingDetails()
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
//    @SerializedName("email")
//    val email: String = ""
//    @SerializedName("mobileNumber")
//    val mobileNumber: String = ""
//    @SerializedName("name")
//    val name: String = ""
//    @SerializedName("password")
//    val password: String = ""
//    @SerializedName("state")
//    val state: String = ""
//    @SerializedName("userType")
//    val userType: String = ""
//    @SerializedName("otp")
//    val otp: String = ""
//    @SerializedName("otpExpireTime")
//    val otpExpireTime: Long = 0
//    @SerializedName("createdAt")
//    val createdAt: String = ""
//    @SerializedName("updatedAt")
//    val updatedAt: String = ""
//    @SerializedName("__v")
//    val v: Int = 0
//    @SerializedName("isReset")
//    val isReset: Boolean = false
//    class DeliveryOrderListLocation {
//        @SerializedName("type")
//        val type: String = ""
//        @SerializedName("coordinates")
//        val coordinates: List<Double> = listOf()
//    }
//
//    class DeliveryOrderListBusinessBankingDetails {
//        @SerializedName("bankName")
//        val bankName: String = ""
//        @SerializedName("accountHolderName")
//        val accountHolderName: String = ""
//        @SerializedName("bank_IFSC")
//        val bankIFSC: String = ""
//        @SerializedName("accountType")
//        val accountType: String = ""
//        @SerializedName("accountNumber")
//        val accountNumber: String = ""
//    }
//}