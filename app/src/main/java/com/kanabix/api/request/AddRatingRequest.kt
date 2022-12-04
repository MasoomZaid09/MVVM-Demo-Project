package com.kanabix.api.request


import com.google.gson.annotations.SerializedName

class AddRatingRequest {
    
    @SerializedName("ratingCount")
    var ratingCount: Int = 0
}