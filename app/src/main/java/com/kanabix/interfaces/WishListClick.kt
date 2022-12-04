package com.kanabix.interfaces

import android.view.View
import android.widget.ImageView

interface WishListClick {
    fun wishListClick(productId :String)
}

interface StoreWishListClick{

    fun storeWishListClick(StoreId :String)
}

interface wishListClickUpdated{
    fun wishListClick(productId: String, view:ImageView,view1:ImageView)
}