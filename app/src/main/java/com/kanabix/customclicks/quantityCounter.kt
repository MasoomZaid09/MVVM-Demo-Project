package com.kanabix.customclicks

import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

interface quantityCounter {
    fun decrement(position: Int, price: String?)
    fun increment(position: Int, price: String?)
}

interface CustomeClick {
    fun click(_id: String?)

}

interface NotificationDeleteListener {
    fun notificationDeleteClick(_id : String)

    fun notificationOpenPopUp(_id: String)
}




interface NotificationViewListener {
    fun notificationViewClick(type : String ,_id : String)
}

interface CustomeClick2 {
    fun click2()
}

interface CustomeClick3 {
    fun click3(_id: String?, categoryName: String?)

}

interface CustomeClick4 {
    fun click4()

}

interface CustomClick5 {
    fun click5(itemId: String, position: Int)
}

interface deleteCartApi {
    fun deleteCartList(itemId: String, position: Int)
}

interface DealsForCustomer {
    fun click(flag: String, flag1: Boolean, _id: String)

}

interface ServicesClick {
    fun click(flag: String)
}

interface deleteClick {
    fun deleteClick()
}

interface popupItemClickListner {
    fun getData(data: String, flag: String)
}

interface servicedeleteclick {
    fun pendingdeleteclick(position: Int)
}

interface CommonInterface {
    fun commomInterface()
}

interface serviceselectedclick {
    fun pendingclick(position: Int)
}

interface subserviceClick {
    fun subservice(flag: String)
}

interface wishlistcustomclick {
    fun wishlist(_id: String?)
}

interface categorynameclick {
    fun categoryname(_id: String?, categoryname: String)
}

interface chooseAddressClick {
    fun AddressClick()
}

interface deleteCartItemListener {
    fun deleteItemClick(
        _id: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView
    )
}

interface cartUpdateListener {
    fun incrementAmount(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView,
        IncAndDecLL: LinearLayout,
        loader: LottieAnimationView

    )

    fun decrementAmount(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView,
        IncAndDecLL: LinearLayout,
        loader: LottieAnimationView
    )

}

interface adaptorCartItemListener {
    fun adaptorItemDeleteClick(
        count: Int?,
        price: Number?,
        id: String?,
        quantity: String,
        tvQuantity: TextView
    )
}

interface editAddressListener {
    fun editAddress(_id : String)
}

interface getAddressIdListener {
    fun getAddressIdClick(_id : String)
}

interface ChooseDeliveryAddressDelete {
    fun deleteClick(id: String,position: Int)
}



