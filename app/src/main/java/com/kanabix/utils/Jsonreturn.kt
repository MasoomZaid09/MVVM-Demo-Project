package com.ayushman.Utils

import org.json.JSONObject




object Jsonreturn {

    fun JsonReturn(string: String) :String
    { var message=""
        try {
            val `object` = JSONObject(string)
             message= `object`.getString("message")
        }
        catch (e:Exception)
        {

        }
        return message
    }
}