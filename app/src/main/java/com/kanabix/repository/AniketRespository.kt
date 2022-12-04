package com.kanabix.repository

import com.google.gson.JsonObject
import com.kanabix.api.Api_Interface_Impl
import com.kanabix.api.request.*
import com.kanabix.api.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class AniketRespository @Inject constructor(private val apiServiceImpl: Api_Interface_Impl) {

    fun CustomerSignIn(request : SignUpRequest): Flow<Response<SignUpResponse>> = flow {
        emit(apiServiceImpl.CustomerSignIn(request))
    }.flowOn(Dispatchers.IO)

    fun CustomerLogin(userType:String, emailOrPhone :String, password:String,deviceType:String, deviceToken : String): Flow<Response<LoginResponse>> = flow {
        emit(apiServiceImpl.CustomerLogin(userType,emailOrPhone,password,deviceType, deviceToken))
    }.flowOn(Dispatchers.IO)


    fun VerifyOtp(request : OTPRequest): Flow<Response<OTPResponse>> = flow {
        emit(apiServiceImpl.VerifyOtp(request))
    }.flowOn(Dispatchers.IO)

    fun ForgetPassword(request : ForgetPasswordRequest): Flow<Response<ForgetPasswordResponse>> = flow {
        emit(apiServiceImpl.ForgetPassword(request))
    }.flowOn(Dispatchers.IO)

    fun ResendOtp(request : ResendOTPRequest): Flow<Response<ResendOTPResponse>> = flow {
        emit(apiServiceImpl.ResendOtp(request))
    }.flowOn(Dispatchers.IO)

    fun ResetPassword(request :ResetPasswordRequest): Flow<Response<ResetPasswordResponse>> = flow {
        emit(apiServiceImpl.ResetPassword(request))
    }.flowOn(Dispatchers.IO)

    fun CountryListApi(): Flow<Response<CountryStateCityListResponse>> = flow {
        emit(apiServiceImpl.CountryListApi())
    }.flowOn(Dispatchers.IO)

    fun StateListApi(countryCode :String,name:String): Flow<Response<CountryStateCityListResponse>> = flow {
        emit(apiServiceImpl.StateListApi(countryCode,name))
    }.flowOn(Dispatchers.IO)

    fun CityListApi(countryCode :String, stateCode : String): Flow<Response<CountryStateCityListResponse>> = flow {
        emit(apiServiceImpl.CityListApi(countryCode, stateCode))
    }.flowOn(Dispatchers.IO)

    fun fillBankDetailsApi(request : BankDetailsRequest): Flow<Response<FillBankDetailsResponse>> = flow {
        emit(apiServiceImpl.FillBankDetails(request))
    }.flowOn(Dispatchers.IO)

    fun SETUSERLOCATION(token: String,userId:String, latitute: Double, latitute1: Double): Flow<Response<LocationResponse>> = flow {
        emit(apiServiceImpl.SetUserLocation(token,userId, latitute,latitute1))
    }.flowOn(Dispatchers.IO)

    fun cartList(search:String,page:Int,limit:Int): Flow<Response<CategoryManagementResponse>> = flow {
        emit(apiServiceImpl.cartList(search,page,limit))
    }.flowOn(Dispatchers.IO)

    fun MyProfileApi(token: String): Flow<Response<MyProfileResponse>> = flow {
        emit(apiServiceImpl.MyProfileApi(token))
    }.flowOn(Dispatchers.IO)

    fun updateProfileApi(token: String, request: JsonObject): Flow<Response<UpdateProfileResponse>> = flow {
        emit(apiServiceImpl.updateProfileApi(token,request))
    }.flowOn(Dispatchers.IO)

    fun changePasswordApi(token: String,oldPassword :String, newPassword :String, confirmPassword :String): Flow<Response<ChangePasswordResponse>> = flow {
        emit(apiServiceImpl.changePasswordApi(token,oldPassword, newPassword, confirmPassword))
    }.flowOn(Dispatchers.IO)

    fun ProductListApi(token: String,request: ProductListRequest): Flow<Response<ProductListResponse>> = flow {
        emit(apiServiceImpl.ProductListApi(token,request))
    }.flowOn(Dispatchers.IO)

    fun wishListApi(token: String): Flow<Response<WishListResponse>> = flow {
        emit(apiServiceImpl.wishListApi(token))
    }.flowOn(Dispatchers.IO)

    fun addWishListApi(token: String,productId :String): Flow<Response<AddWishListResponse>> = flow {
        emit(apiServiceImpl.addWishListApi(token,productId))
    }.flowOn(Dispatchers.IO)


    fun viewProductApi(token: String,productId :String): Flow<Response<ViewProductResponse>> = flow {
        emit(apiServiceImpl.viewProductApi(token,productId))
    }.flowOn(Dispatchers.IO)

    fun viewDealApi(token: String,dealId :String): Flow<Response<ViewDealResponse>> = flow {
        emit(apiServiceImpl.viewDealApi(token,dealId))
    }.flowOn(Dispatchers.IO)

    fun homeApi(): Flow<Response<HomeResponse>> = flow {
        emit(apiServiceImpl.homeApi())
    }.flowOn(Dispatchers.IO)

    fun productAddToCartApi(token: String,jsonObject: JsonObject): Flow<Response<ProductAddToCartResponse>> = flow {
        emit(apiServiceImpl.productAddToCartApi(token,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun CartListApi(token: String): Flow<Response<CartListResponse>> = flow {
        emit(apiServiceImpl.CartListApi(token))
    }.flowOn(Dispatchers.IO)

    fun DeleteCartItemApi(token: String,_id :String): Flow<Response<DeleteCartItemResponse>> = flow {
        emit(apiServiceImpl.DeleteCartItemApi(token,_id))
    }.flowOn(Dispatchers.IO)

    fun dealListApi(lat:Double,lng:Double,search :String, categoryId :String,page:Int,limit:Int): Flow<Response<DealListResponse>> = flow {
        emit(apiServiceImpl.dealListApi(lat,lng,search,categoryId,page, limit))
    }.flowOn(Dispatchers.IO)


    fun updateCartItemApi(token : String, _id : String,jsonObject: JsonObject): Flow<Response<UpdateCartItemResponse>> = flow {
        emit(apiServiceImpl.updateCartItemApi(token , _id,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun AddAddressApi(token : String, jsonObject: JsonObject): Flow<Response<AddAddressResponse>> = flow {
        emit(apiServiceImpl.AddAddressApi(token ,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun EditAddressApi(token : String, jsonObject: JsonObject): Flow<Response<EditAddressResponse>> = flow {
        emit(apiServiceImpl.EditAddressApi(token ,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun ListAddressApi(token : String, jsonObject: JsonObject): Flow<Response<AddressListResponse>> = flow {
        emit(apiServiceImpl.ListAddressApi(token ,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun OrderCreateApi(token: String, jsonObject: FinalCreateOrderRequest): Flow<Response<OrderCreateResponse>> = flow {
        emit(apiServiceImpl.OrderCreateApi(token ,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun ViewAddressApi(token : String, addressId: String): Flow<Response<ViewAddressResponse>> = flow {
        emit(apiServiceImpl.ViewAddressApi(token ,addressId))
    }.flowOn(Dispatchers.IO)

    fun appRatingApi(token:String,request : AddRatingRequest): Flow<Response<RatingResponse>> = flow {
        emit(apiServiceImpl.appRatingApi(token, request))
    }.flowOn(Dispatchers.IO)

    fun termListApi(): Flow<Response<TermsResponse>> = flow {
        emit(apiServiceImpl.termListApi())
    }.flowOn(Dispatchers.IO)

    fun faqListApi(): Flow<Response<FaqsResponse>> = flow {
        emit(apiServiceImpl.faqListApi())
    }.flowOn(Dispatchers.IO)

    fun orderListApi(token:String,search: String): Flow<Response<OrderListResponse>> = flow {
        emit(apiServiceImpl.orderListApi(token,search))
    }.flowOn(Dispatchers.IO)

    fun viewOrderApi(token:String,orderId :String): Flow<Response<ViewOrderResponse>> = flow {
        emit(apiServiceImpl.viewOrderApi(token,orderId))
    }.flowOn(Dispatchers.IO)


    fun bannerListApi(): Flow<Response<BannerResponse>> = flow {
        emit(apiServiceImpl.bannerListApi())
    }.flowOn(Dispatchers.IO)

    fun storeListApi(token :String,request: StoreListRequest): Flow<Response<StoreListResponse>> = flow {
        emit(apiServiceImpl.storeListApi(token,request))
    }.flowOn(Dispatchers.IO)

    fun addStoreWishListApi(token: String,storeId :String): Flow<Response<AddStoreLikeResponse>> = flow {
        emit(apiServiceImpl.addStoreWishListApi(token,storeId))
    }.flowOn(Dispatchers.IO)

    fun viewStoreApi(token: String,storeId :String): Flow<Response<ViewStoreResponse>> = flow {
        emit(apiServiceImpl.viewStoreApi(token,storeId))
    }.flowOn(Dispatchers.IO)

    fun storeProductListApi(token: String, storeId :String,search:String, page:Int,limit: Int): Flow<Response<StoreProductsListResponse>> = flow {
        emit(apiServiceImpl.storeProductListApi(token,storeId,search,page,limit))
    }.flowOn(Dispatchers.IO)

    fun viewBankDetails(token: String): Flow<Response<ViewBankDetailsResponse>> = flow {
        emit(apiServiceImpl.viewBankDetails(token))
    }.flowOn(Dispatchers.IO)

    fun editBankDetails(token: String,request: EditBankDetailsRequest): Flow<Response<EditBankDetailsResponse>> = flow {
        emit(apiServiceImpl.editBankDetails(token,request))
    }.flowOn(Dispatchers.IO)

    fun likedStoreList(token: String,page:Int,limit: Int): Flow<Response<StoreLikeListResponse>> = flow {
        emit(apiServiceImpl.likedStoreList(token,page, limit))
    }.flowOn(Dispatchers.IO)

    // karan sir api

    fun NotificationListApi(token: String): Flow<Response<NotificationListResponse>> = flow {
        emit(apiServiceImpl.NotificationListApi(token))
    }.flowOn(Dispatchers.IO)

    fun NotificationClearAllApi(token: String): Flow<Response<NotificationDeleteResponse>> = flow {
        emit(apiServiceImpl.NotificationClearAllApi(token))
    }.flowOn(Dispatchers.IO)

    fun NotificationDeleteApi(token: String, _id : String): Flow<Response<NotificationDeleteResponse>> = flow {
        emit(apiServiceImpl.NotificationDeleteApi(token,_id))
    }.flowOn(Dispatchers.IO)

    fun NotificationViewApi(token: String, _id : String): Flow<Response<NotificationViewResponse>> = flow {
        emit(apiServiceImpl.NotificationViewApi(token,_id))
    }.flowOn(Dispatchers.IO)

    fun DeleteAddressApi(token : String, _id : String): Flow<Response<EditAddressResponse>> = flow {
        emit(apiServiceImpl.DeleteAddressApi(token ,_id))
    }.flowOn(Dispatchers.IO)

    fun uploadFileApi(uploadFile: MultipartBody.Part?): Flow<Response<UploadFileResponse>> = flow {
        emit(apiServiceImpl.uploadFileApi(uploadFile))
    }.flowOn(Dispatchers.IO)

    fun uploadDocumentApi(uploadFile: MultipartBody.Part?): Flow<Response<UploadFileResponse>> = flow {
        emit(apiServiceImpl.uploadDocumentApi(uploadFile))
    }.flowOn(Dispatchers.IO)

    fun deliveryPartnerOrderList(token :String, deliveryPartnerStatus : String, search: String): Flow<Response<DeliveryOrderListResponse>> = flow {
        emit(apiServiceImpl.deliveryPartnerOrderList(token,deliveryPartnerStatus, search))
    }.flowOn(Dispatchers.IO)

    fun acceptRejectOrderApi(
        token: String,
        orderId: String,
        deliveryPartnerStatus: String,
        reason: String,
        pickupDate: String?,
        deliveryDateAndTime: String?
    ): Flow<Response<AcceptDeclineOrderResponse>> = flow {
        emit(apiServiceImpl.acceptRejectOrderApi(token,orderId, deliveryPartnerStatus,reason,pickupDate, deliveryDateAndTime))
    }.flowOn(Dispatchers.IO)

    fun viewOrderDeliveryApi(token:String,orderId :String): Flow<Response<ViewOrderResponse>> = flow {
        emit(apiServiceImpl.viewOrderDeliveryApi(token,orderId))
    }.flowOn(Dispatchers.IO)

    fun createOrderTrackingApi(token:String,request: CreateOrderTrackingRequest): Flow<Response<CreateOrderTrackingResponse>> = flow {
        emit(apiServiceImpl.createOrderTrackingApi(token,request))
    }.flowOn(Dispatchers.IO)

    fun orderOTPVerify(token:String,orderId :String,otp : String): Flow<Response<VerifyOrderTrackingResponse>> = flow {
        emit(apiServiceImpl.orderOTPVerify(token,orderId,otp))
    }.flowOn(Dispatchers.IO)

    fun orderOTPResend(token:String,orderId :String): Flow<Response<ResendOrderOtpResponse>> = flow {
        emit(apiServiceImpl.orderOTPResend(token,orderId))
    }.flowOn(Dispatchers.IO)

    fun checkOutApi(token:String,jsonObject: checkOutRequest): Flow<Response<checkOutResponse>> = flow {
        emit(apiServiceImpl.checkOutApi(token,jsonObject))
    }.flowOn(Dispatchers.IO)

    fun verifyPaymentApi(jsonObject: JsonObject): Flow<Response<VerifyPaymentResponse>> = flow {
        emit(apiServiceImpl.verifyPaymentApi(jsonObject))
    }.flowOn(Dispatchers.IO)

    fun paymentListApi(token: String): Flow<Response<PaymentListResponse>> = flow {
        emit(apiServiceImpl.paymentListApi(token))
    }.flowOn(Dispatchers.IO)

    fun paymentViewApi(token: String,_id: String): Flow<Response<PaymentViewResponse>> = flow {
        emit(apiServiceImpl.paymentViewApi(token,_id))
    }.flowOn(Dispatchers.IO)

    fun checkQuantityApi(token: String): Flow<Response<checkQuantityPaymentResponse>> = flow {
        emit(apiServiceImpl.checkQuantityApi(token))
    }.flowOn(Dispatchers.IO)

    fun logOutApi(token: String,deviceToken: String,deviceType: String): Flow<Response<LogOutResponse>> = flow {
        emit(apiServiceImpl.logOutApi(token,deviceToken, deviceType))
    }.flowOn(Dispatchers.IO)

}