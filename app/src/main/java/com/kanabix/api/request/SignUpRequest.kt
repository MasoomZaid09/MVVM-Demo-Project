package com.kanabix.api.request

import com.google.gson.annotations.SerializedName

class SignUpRequest {
    @SerializedName("userType")
    var userType: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("countryCode")
    var countryCode: String = ""
    @SerializedName("mobileNumber")
    var mobileNumber: String = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("address")
    var address: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("city")
    var city: String = ""
    @SerializedName("state")
    var state: String = ""
    @SerializedName("country")
    var country: String = ""
    @SerializedName("zipCode")
    var zipCode: String = ""
    @SerializedName("govtDocument")
    var govtDocument: String = ""
    @SerializedName("countryIsoCode")
    var countryIsoCode: String = ""
    @SerializedName("stateIsoCode")
    var stateIsoCode: String = ""
    @SerializedName("fileName")
    var fileName: String = ""
}