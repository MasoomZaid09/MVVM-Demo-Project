package com.kanabix.utils


import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object TakePictureUtils {

    @Throws(IOException::class)
    fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

}