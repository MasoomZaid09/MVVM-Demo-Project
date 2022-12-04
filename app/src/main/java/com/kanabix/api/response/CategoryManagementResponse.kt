package com.kanabix.api.response


import com.google.gson.annotations.SerializedName

class CategoryManagementResponse {
    @SerializedName("result")
    val result: CategoryManagementResult = CategoryManagementResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0

}

class CategoryManagementResult {
    @SerializedName("page")
    val page: Int = 0
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("pages")
    val pages: Int = 0
    @SerializedName("docs")
    val docs: ArrayList<CategoryManagementDoc> = ArrayList()
}

class CategoryManagementDoc {
    @SerializedName("status")
    val status: String = ""
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("categoryName")
    val categoryName: String = ""
    @SerializedName("categoryImage")
    val categoryImage: String = ""
    @SerializedName("description")
    val description: String = ""
    @SerializedName("thumbnail")
    val thumbnail: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}