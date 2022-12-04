package com.kanabix.api.request


import com.google.gson.annotations.SerializedName
import com.kanabix.api.response.ViewAddressResponse

class FinalCreateOrderRequest {
    @SerializedName("orderDetails")
    var orderDetails: ArrayList<OrderCreateRequest> = ArrayList()
}
class OrderCreateRequest {

    @SerializedName("actualPrice")
    var actualPrice: Number = 0
    @SerializedName("dealsDiscount")
    var dealsDiscount: Number = 0
    @SerializedName("taxPrice")
    var taxPrice: Number = 0
    @SerializedName("orderPrice")
    var orderPrice: Number = 0
    @SerializedName("deliveryFee")
    var deliveryFee: Number = 0
    @SerializedName("address")
    val address: String = ""
    @SerializedName("merchantId")
    var merchantId: String = ""
    @SerializedName("cartId")
    var cartId: ArrayList<String> = ArrayList()
    @SerializedName("shippingFixedAddress")
    var shippingFixedAddress: ViewAddressResponse.ViewAddressResult = ViewAddressResponse.ViewAddressResult()

}