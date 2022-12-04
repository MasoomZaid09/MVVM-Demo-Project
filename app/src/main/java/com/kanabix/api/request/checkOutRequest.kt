package com.kanabix.api.request


import com.google.gson.annotations.SerializedName
import com.kanabix.api.response.ViewAddressResponse

class checkOutRequest {
    @SerializedName("currencyCode")
    var currencyCode: String = ""
    @SerializedName("price")
    var price: Int = 0
    @SerializedName("orderIds")
    var orderIds: ArrayList<String> = ArrayList()
    @SerializedName("shippingFixedAddress")
    var shippingFixedAddress: ViewAddressResponse.ViewAddressResult =
        ViewAddressResponse.ViewAddressResult()
}
