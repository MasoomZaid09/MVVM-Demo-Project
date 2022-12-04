package com.kanabix.api

import com.google.gson.JsonObject
import com.kanabix.api.request.*
import com.kanabix.api.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface Api_Interface {

    @POST("user/signUp")
    suspend fun CustomerSignIn(@Body request: SignUpRequest): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("user/usersLogin")
    suspend fun CustomerLogin(
        @Field("userType") userType: String,
        @Field("emailNumberId") emailOrPhone: String,
        @Field("password") password: String,
        @Field("deviceType") deviceType: String,
        @Field("deviceToken") deviceToken: String
    ): Response<LoginResponse>

    @POST("user/forgotPassword")
    suspend fun ForgetPassword(@Body request: ForgetPasswordRequest): Response<ForgetPasswordResponse>

    @POST("user/verifyOTP")
    suspend fun VerifyOtp(@Body request: OTPRequest): Response<OTPResponse>

    @POST("user/resendOTP")
    suspend fun ResendOtp(@Body request: ResendOTPRequest): Response<ResendOTPResponse>


    @PUT("user/resetPassword")
    suspend fun ResetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>


    @GET("admin/getAllCountry")
    suspend fun CountryListApi(): Response<CountryStateCityListResponse>

    @GET("admin/getAllStates")
    suspend fun StateListApi(@Query("countryCode") countryCode: String,@Query("name") name: String): Response<CountryStateCityListResponse>

    @GET("admin/getAllCities")
    suspend fun CityListApi(
        @Query("countryCode") countryCode: String,
        @Query("stateCode") stateCode: String
    ): Response<CountryStateCityListResponse>

    @POST("user/fillDetails")
    suspend fun FillBankDetailsApi(@Body request: BankDetailsRequest): Response<FillBankDetailsResponse>

    @FormUrlEncoded
    @PUT("user/setLocation")
    suspend fun SetUserLocation(
        @Header("token") token: String,
        @Query("userId") userId: String,
        @Field("lat") latitude: Double,
        @Field("long") longitude: Double
    ): Response<LocationResponse>

    @FormUrlEncoded
    @POST("user/listCategory")
    suspend fun cartList(
        @Field("search") search: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<CategoryManagementResponse>

    @GET("user/myProfile")
    suspend fun myprofileapi(@Header("token") token: String): Response<MyProfileResponse>

    @PUT("user/updateProfile")
    suspend fun updateProfileApi(
        @Header("token") token: String,
        @Body request: JsonObject
    ): Response<UpdateProfileResponse>

    @FormUrlEncoded
    @PUT("user/changePassword")
    suspend fun changePasswordApi(
        @Header("token") token: String,
        @Field("password") password: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmPassword") confirmPassword: String
    ): Response<ChangePasswordResponse>

    @POST("user/productListForAll")
    suspend fun ProductListApi(
        @Header("token") token: String,
        @Body request: ProductListRequest
    ): Response<ProductListResponse>

    @GET("user/wishList")
    suspend fun wishListApi(@Header("token") token: String): Response<WishListResponse>

    @POST("user/addWishListProduct")
    suspend fun addWishListApi(
        @Header("token") token: String,
        @Query("productId") productId: String
    ): Response<AddWishListResponse>

    @POST("cart/productAddToCart")
    suspend fun productAddToCartApi(
        @Header("token") token: String,
        @Body jsonObject: JsonObject

    ): Response<ProductAddToCartResponse>

    @GET("cart/cartList")
    suspend fun CartListApi(
        @Header("token") token: String
    ): Response<CartListResponse>

    @DELETE("cart/removeItemFromCart")
    suspend fun DeleteCartItemApi(
        @Header("token") token: String,
        @Query("cartId") _id: String,
    ): Response<DeleteCartItemResponse>

    @POST("product/viewProduct")
    suspend fun viewProductApi(@Header("token")token: String,@Query("productId") productId: String): Response<ViewProductResponse>

    @GET("user/viewDeal")
    suspend fun viewDealApi(@Header("token")token :String,@Query("dealId") dealId: String): Response<ViewDealResponse>

    @GET("user/homePage")
    suspend fun homeApi(): Response<HomeResponse>

    @FormUrlEncoded
    @POST("deal/listDealByLocation")
    suspend fun dealListApi(
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
        @Field("search") search: String,
        @Field("categoryId") categoryId: String,
        @Field("page") page: Int,
        @Field("limit") limit: Int
    ): Response<DealListResponse>


    @PUT("cart/updateCartItem")
    suspend fun updateCartItemApi(
        @Header("token") token: String?,
        @Query("cartId") id: String,
        @Body jsonObject: JsonObject
    ): Response<UpdateCartItemResponse>

    @POST("address/addAddress")
    suspend fun AddAddressApi(
        @Header("token") token: String?,
        @Body jsonObject: JsonObject
    ): Response<AddAddressResponse>

    @PUT("address/editAddress")
    suspend fun EditAddressApi(
        @Header("token") token: String?,
        @Body jsonObject: JsonObject
    ): Response<EditAddressResponse>

    @POST("address/listAddress")
    suspend fun ListAddressApi(
        @Header("token") token: String?,
        @Body jsonObject: JsonObject
    ): Response<AddressListResponse>

    @POST("order/generateMultipleOrders")
    suspend fun OrderCreateApi(
        @Header("token") token: String?,
        @Body jsonObject: FinalCreateOrderRequest
    ): Response<OrderCreateResponse>

    @GET("address/viewAddress")
    suspend fun ViewAddressApi(
        @Header("token") token: String?,
        @Query("addressId") addressId: String
    ): Response<ViewAddressResponse>

    @POST("user/addAppRating")
    suspend fun appRatingApi(
        @Header("token") token: String,
        @Body request: AddRatingRequest
    ): Response<RatingResponse>

    @GET("static/viewTermAndCond")
    suspend fun termListApi(): Response<TermsResponse>

    @GET("static/faqList")
    suspend fun faqListApi(): Response<FaqsResponse>

    @FormUrlEncoded
    @POST("order/orderList")
    suspend fun orderListApi(@Header("token")token :String, @Field("search") search:String) : Response<OrderListResponse>

    @GET("order/viewOrder")
    suspend fun viewOrderApi(@Header("token")token :String, @Query("orderId")orderId :String) : Response<ViewOrderResponse>

    // 07 September Api with home api

    @GET("user/listBanner")
    suspend fun bannerListApi() : Response<BannerResponse>

    @POST("user/storeNearMe")
    suspend fun storeListApi(@Header("token")token :String,@Body request: StoreListRequest) : Response<StoreListResponse>

    @FormUrlEncoded
    @POST("user/likeOrUnlikeStore")
    suspend fun addStoreWishListApi(
        @Header("token") token: String,
        @Field("storeId") storeId: String
    ): Response<AddStoreLikeResponse>

    @GET("user/viewStore")
    suspend fun viewStoreApi(
        @Header("token") token: String,
        @Query("storeId") storeId: String
    ): Response<ViewStoreResponse>

    @FormUrlEncoded
    @POST("user/listProductByStore")
    suspend fun storeProductListApi(@Header("token")token: String,@Field("storeId")storeId:String,@Field("search")search:String,@Field("page")page:Int,@Field("limit")limit:Int ) : Response<StoreProductsListResponse>

    @GET("user/viewBankDetails")
    suspend fun viewBankDetails(@Header("token")token: String) :Response<ViewBankDetailsResponse>

    @PUT("user/editBankDetails")
    suspend fun editBankDetails(@Header("token")token: String, @Body request: EditBankDetailsRequest) :Response<EditBankDetailsResponse>

    @POST("user/likedStoreList")
    suspend fun likedStoreList(@Header("token")token: String, @Query("page")page :Int, @Query("limit")limit :Int) :Response<StoreLikeListResponse>


    // karan sir api

    @POST("user/listNotification")
    suspend fun NotificationListApi(@Header("token") token: String): Response<NotificationListResponse>

    @GET("user/clearNotification")
    suspend fun NotificationClearAllApi(@Header("token") token: String): Response<NotificationDeleteResponse>

    @DELETE("user/deleteNotification")
    suspend fun NotificationDeleteApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<NotificationDeleteResponse>

    @GET("user/viewParticularNotification")
    suspend fun NotificationViewApi(
        @Header("token") token: String,
        @Query("_id") _id: String
    ): Response<NotificationViewResponse>

    @DELETE("address/deleteAddress")
    suspend fun DeleteAddressApi(
        @Header("token") token: String?,
        @Query("addressId") _id: String
    ): Response<EditAddressResponse>

    @Multipart
    @POST("user/uploadFile")
    suspend fun uploadFileApi(@Part file: MultipartBody.Part?): Response<UploadFileResponse>

    @Multipart
    @POST("user/uploadDocument")
    suspend fun uploadDocumentApi(@Part file: MultipartBody.Part?): Response<UploadFileResponse>


    // Delivery Partner Order Tab Apis
    @FormUrlEncoded
    @POST("order/deliverlyPartnerOrderList")
    suspend fun deliveryPartnerOrderList(@Header("token")token :String, @Field("deliveryPartnerStatus")deliveryPartnerStatus :String,@Field("search")search :String) : Response<DeliveryOrderListResponse>

    @FormUrlEncoded
    @POST("order/acceptRejectDelivery")
    suspend fun acceptRejectOrderApi(
        @Header("token") token: String,
        @Field("orderId") orderId: String,
        @Field("deliveryPartnerStatus") deliveryPartnerStatus: String,
        @Field("reason") reason: String,
        @Field("pickUpDate") pickUpDate: String?,
        @Field("deliveryDateAndTime") deliveryDateAndTime: String?,
    ) : Response<AcceptDeclineOrderResponse>


    @GET("order/viewOrderByDeliveryPartner")
    suspend fun viewOrderDeliveryApi(@Header("token")token :String, @Query("orderId")orderId :String) : Response<ViewOrderResponse>

    @POST("tracking/createTracking")
    suspend fun createOrderTrackingApi(@Header("token")token :String, @Body request: CreateOrderTrackingRequest) : Response<CreateOrderTrackingResponse>

    @PUT("tracking/verifyDeliveryOrder")
    suspend fun orderOTPVerify(@Header("token")token :String, @Query("orderId")orderId :String,@Query("orderOtp")orderOtp :String ) : Response<VerifyOrderTrackingResponse>

    @POST("tracking/resendOtpForDelivery")
    suspend fun orderOTPResend(@Header("token")token :String, @Query("orderId")orderId :String) : Response<ResendOrderOtpResponse>

    @POST("order/checkOut")
    suspend fun checkOutApi(@Header("token")token :String,@Body request: checkOutRequest) : Response<checkOutResponse>

    @POST("order/verifyPayment")
    suspend fun verifyPaymentApi(@Body jsonObject: JsonObject) : Response<VerifyPaymentResponse>

    @POST("order/paymentListOfPartner")
    suspend fun paymentListApi(@Header ("token")token : String) : Response<PaymentListResponse>

    @GET("order/viewPaymentOfPartner")
    suspend fun paymentViewApi(@Header ("token")token : String, @Query("_id")_id: String) : Response<PaymentViewResponse>

    @GET("order/checkRequiredQuantity")
    suspend fun checkQuantityApi(@Header ("token")token : String) : Response<checkQuantityPaymentResponse>

    @FormUrlEncoded
    @POST("user/logout")
    suspend fun logOutApi(@Header("token")token : String, @Field("deviceToken") deviceToken:String, @Field("deviceType")deviceType : String) : Response<LogOutResponse>

}