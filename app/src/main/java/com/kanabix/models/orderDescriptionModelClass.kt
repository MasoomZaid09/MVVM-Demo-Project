package com.kanabix.models

data class orderDescriptionModelClass (
    val itemName : String = "",
    val itemDesc : String="",
    val itemImage : Int = 0,
    var statusFlag : Boolean = false,
    var statusName : String = "",
    var createdAt : String = "",
)