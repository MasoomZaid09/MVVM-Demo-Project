package com.kanabix.interfaces

interface addressDelete{
    fun deleteAdd()
}

interface addressDeleteListener{
    fun deleteAdd(id: String,position :Int)
}