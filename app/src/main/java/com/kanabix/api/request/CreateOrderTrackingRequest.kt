package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class CreateOrderTrackingRequest {
    @SerializedName("transporter")
    var transporter: CreateOrderTrackingTransporter = CreateOrderTrackingTransporter()
    @SerializedName("msg")
    var msg: String = ""
    @SerializedName("statusOfTracking")
    var statusOfTracking: String = ""
    @SerializedName("orderId")
    var orderId: String = ""

}

class CreateOrderTrackingTransporter