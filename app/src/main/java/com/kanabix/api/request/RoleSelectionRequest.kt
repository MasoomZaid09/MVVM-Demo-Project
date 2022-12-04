package com.kanabix.api.request

import com.google.gson.annotations.SerializedName

class RoleSelectionRequest {

    @SerializedName("mobileNo")
    var mobileNo: String = ""
    @SerializedName("role")
    var role: String = ""
}