package com.kanabix.api

import com.google.gson.JsonObject
import com.kanabix.api.request.*
import com.kanabix.api.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class Api_Interface_Impl @Inject constructor(private val apiService: Api_Interface) {

    suspend fun CustomerSignIn(request : SignUpRequest): Response<SignUpResponse> = apiService.CustomerSignIn(request)

    suspend fun CustomerLogin(userType:String, emailOrPhone :String, password:String, deviceType:String, deviceToken : String): Response<LoginResponse> = apiService.CustomerLogin(userType,emailOrPhone,password,deviceType, deviceToken)

    suspend fun ForgetPassword(request: ForgetPasswordRequest): Response<ForgetPasswordResponse> = apiService.ForgetPassword(request)


    suspend fun VerifyOtp(request: OTPRequest): Response<OTPResponse> = apiService.VerifyOtp(request)


    suspend fun ResendOtp(request: ResendOTPRequest): Response<ResendOTPResponse> = apiService.ResendOtp(request)


    suspend fun ResetPassword(request : ResetPasswordRequest): Response<ResetPasswordResponse> = apiService.ResetPassword(request)

    suspend fun CountryListApi(): Response<CountryStateCityListResponse> = apiService.CountryListApi()

    suspend fun StateListApi(countryCode :String,name:String): Response<CountryStateCityListResponse> = apiService.StateListApi(countryCode,name)

    suspend fun CityListApi(countryCode :String,stateCode :String): Response<CountryStateCityListResponse> = apiService.CityListApi(countryCode, stateCode)

    suspend fun FillBankDetails(request : BankDetailsRequest):Response<FillBankDetailsResponse> = apiService.FillBankDetailsApi(request)

    suspend fun SetUserLocation(token: String,userId:String, latitute: Double, latitute1: Double): Response<LocationResponse> = apiService.SetUserLocation(token,userId, latitute,latitute1)

    suspend fun cartList(search:String,page:Int,limit:Int): Response<CategoryManagementResponse> = apiService.cartList(search,page,limit)

    suspend fun MyProfileApi(token: String): Response<MyProfileResponse> = apiService.myprofileapi(token)

    suspend fun updateProfileApi(token: String, request: JsonObject): Response<UpdateProfileResponse> = apiService.updateProfileApi(token,request)

    suspend fun ProductListApi(token: String, request: ProductListRequest): Response<ProductListResponse> = apiService.ProductListApi(token,request)

    suspend fun changePasswordApi(token: String, password: String , newPassword:String, confrimPassword :String): Response<ChangePasswordResponse> = apiService.changePasswordApi(token,password,newPassword,confrimPassword)

    suspend fun wishListApi(token: String): Response<WishListResponse> = apiService.wishListApi(token)

    suspend fun addWishListApi(token: String, productId :String): Response<AddWishListResponse> = apiService.addWishListApi(token,productId)

    suspend fun productAddToCartApi(token: String, jsonObject: JsonObject): Response<ProductAddToCartResponse> = apiService.productAddToCartApi(token,jsonObject)

    suspend fun CartListApi(token: String): Response<CartListResponse> = apiService.CartListApi(token)

    suspend fun DeleteCartItemApi(token: String, _id : String): Response<DeleteCartItemResponse> = apiService.DeleteCartItemApi(token,_id)

    suspend fun viewProductApi(token: String,productId :String): Response<ViewProductResponse> = apiService.viewProductApi(token,productId)

    suspend fun viewDealApi(token: String,dealId :String): Response<ViewDealResponse> = apiService.viewDealApi(token,dealId)

    suspend fun homeApi(): Response<HomeResponse> = apiService.homeApi()

    suspend fun updateCartItemApi(token : String,_id : String, jsonObject: JsonObject): Response<UpdateCartItemResponse> = apiService.updateCartItemApi(token, _id,jsonObject)

    suspend fun AddAddressApi(token : String, jsonObject: JsonObject): Response<AddAddressResponse> = apiService.AddAddressApi(token,jsonObject)

    suspend fun EditAddressApi(token : String, jsonObject: JsonObject): Response<EditAddressResponse> = apiService.EditAddressApi(token,jsonObject)

    suspend fun ListAddressApi(token : String, jsonObject: JsonObject): Response<AddressListResponse> = apiService.ListAddressApi(token,jsonObject)


    suspend fun OrderCreateApi(token: String, jsonObject: FinalCreateOrderRequest): Response<OrderCreateResponse> = apiService.OrderCreateApi(token,jsonObject)

    suspend fun ViewAddressApi(token : String, addressId: String): Response<ViewAddressResponse> = apiService.ViewAddressApi(token,addressId)

    suspend fun dealListApi(lat:Double,lng:Double,search :String, categoryId :String, page:Int,limit:Int): Response<DealListResponse> = apiService.dealListApi(lat,lng,search, categoryId,page,limit)

    suspend fun appRatingApi(token:String,request : AddRatingRequest): Response<RatingResponse> = apiService.appRatingApi(token, request)

    suspend fun termListApi(): Response<TermsResponse> = apiService.termListApi()

    suspend fun faqListApi(): Response<FaqsResponse> = apiService.faqListApi()

    suspend fun orderListApi(token:String,search: String): Response<OrderListResponse> = apiService.orderListApi(token,search)

    suspend fun viewOrderApi(token:String,orderId :String): Response<ViewOrderResponse> = apiService.viewOrderApi(token,orderId)


    suspend fun bannerListApi(): Response<BannerResponse> = apiService.bannerListApi()

    suspend fun storeListApi(token :String,request: StoreListRequest): Response<StoreListResponse> = apiService.storeListApi(token,request)

    suspend fun addStoreWishListApi(token: String, storeId :String): Response<AddStoreLikeResponse> = apiService.addStoreWishListApi(token,storeId)

    suspend fun viewStoreApi(token: String, storeId :String): Response<ViewStoreResponse> = apiService.viewStoreApi(token,storeId)

    suspend fun storeProductListApi(token: String, storeId :String,search:String, page:Int,limit: Int): Response<StoreProductsListResponse> = apiService.storeProductListApi(token,storeId,search,page,limit)

    suspend fun viewBankDetails(token: String): Response<ViewBankDetailsResponse> = apiService.viewBankDetails(token)

    suspend fun editBankDetails(token: String,request: EditBankDetailsRequest): Response<EditBankDetailsResponse> = apiService.editBankDetails(token,request)

    suspend fun likedStoreList(token: String,page:Int,limit: Int): Response<StoreLikeListResponse> = apiService.likedStoreList(token,page, limit)

    // karan sir api
    suspend fun NotificationListApi(token:String): Response<NotificationListResponse> = apiService.NotificationListApi(token)

    suspend fun NotificationClearAllApi(token:String): Response<NotificationDeleteResponse> = apiService.NotificationClearAllApi(token)

    suspend fun NotificationDeleteApi(token:String, id:String): Response<NotificationDeleteResponse> = apiService.NotificationDeleteApi(token, id)

    suspend fun NotificationViewApi(token:String, id:String): Response<NotificationViewResponse> = apiService.NotificationViewApi(token, id)

    suspend fun DeleteAddressApi(token : String, _id: String): Response<EditAddressResponse> = apiService.DeleteAddressApi(token,_id)

    suspend fun uploadFileApi(uploadFile: MultipartBody.Part?): Response<UploadFileResponse> = apiService.uploadFileApi(uploadFile)

    suspend fun uploadDocumentApi(uploadFile: MultipartBody.Part?): Response<UploadFileResponse> = apiService.uploadDocumentApi(uploadFile)

    suspend fun deliveryPartnerOrderList(token :String, deliveryPartnerStatus : String, search: String): Response<DeliveryOrderListResponse> = apiService.deliveryPartnerOrderList(token, deliveryPartnerStatus, search)

    suspend fun acceptRejectOrderApi(
        token: String,
        orderId: String,
        deliveryPartnerStatus: String,
        reason: String,
        pickupDate: String?,
        deliveryDateAndTime: String?
    ): Response<AcceptDeclineOrderResponse> = apiService.acceptRejectOrderApi(token, orderId, deliveryPartnerStatus,reason,pickupDate,deliveryDateAndTime)

    suspend fun viewOrderDeliveryApi(token:String,orderId :String): Response<ViewOrderResponse> = apiService.viewOrderDeliveryApi(token,orderId)

    suspend fun createOrderTrackingApi(token:String, request: CreateOrderTrackingRequest): Response<CreateOrderTrackingResponse> = apiService.createOrderTrackingApi(token,request)

    suspend fun orderOTPVerify(token:String,orderId :String, otp: String): Response<VerifyOrderTrackingResponse> = apiService.orderOTPVerify(token,orderId,otp)

    suspend fun orderOTPResend(token:String,orderId :String): Response<ResendOrderOtpResponse> = apiService.orderOTPResend(token,orderId)

    suspend fun checkOutApi(token:String,jsonObject: checkOutRequest): Response<checkOutResponse> = apiService.checkOutApi(token,jsonObject)

    suspend fun verifyPaymentApi(jsonObject: JsonObject): Response<VerifyPaymentResponse> = apiService.verifyPaymentApi(jsonObject)

    suspend fun paymentListApi(token: String): Response<PaymentListResponse> = apiService.paymentListApi(token)

    suspend fun paymentViewApi(token: String,_id: String): Response<PaymentViewResponse> = apiService.paymentViewApi(token,_id)

    suspend fun checkQuantityApi(token: String): Response<checkQuantityPaymentResponse> = apiService.checkQuantityApi(token)

    suspend fun logOutApi(token: String,deviceToken: String,deviceType: String): Response<LogOutResponse> = apiService.logOutApi(token,deviceToken, deviceType)

}

