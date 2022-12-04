    package com.kanabix.interfaces

    import com.kanabix.api.response.ProductListResponse


    interface WishListUpdateClick {
    fun updateWishList()
}

interface ProductClick {
    fun sendProductId(productId:String)
}

interface paymentDialogClickListener {
    fun paymentDialogClick()
}

interface StoreClick {
    fun sendStoreId(storeId:String)
}
interface bottomSheetClick{
    fun bottomSheetListener()
}

interface DealProductClick {
    fun DealProductClick(dealId :String)

}

interface PaymentManagementListener{
    fun paymentListener(_id:String)
}

interface PermissionDeniedListener {

    fun permissionDeniedListener()
}


interface PaymentManagement{
    fun PaymentManagementClick(position: Int)
}

interface OrderManagementClick{
    fun orderManagementClick(orderId :String)
}


interface CategoryClick{
    fun categoryClick(categoryId: String)
}

interface RateAppClick{
    fun rateThisApp(rating: Int)
}

interface LogOutListener{
    fun logOutListener(roleFlag: String)
}

interface sessionExpiredListener{

    fun sessionExpiredClick()
}
    interface IView {
        fun onInitializeRecyclerView()
        val token: String?

        fun resultsEventsFromWebDb(body: ProductListResponse?)
        fun resultsOnLoadNextEvents(body: ProductListResponse?)
    }