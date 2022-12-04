package com.kanabix.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class CustomImageUtil {
    companion object {
        fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
            val result: String
            val cursor: Cursor =
                context.getContentResolver().query(contentURI, null, null, null, null)!!
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath()!!
            } else {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }

        // method for bitmap to base64
        fun encodeTobase64(image: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 60, baos)
            val b: ByteArray = baos.toByteArray()
            val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)

            return imageEncoded
        }

        /**
         * getExifOrientation -- Roate the image on the right angel
         * @param filepath -- path of the file to be rotated
         * @return
         */
        fun getExifOrientation(filepath: String?): Int {
            var degree = 0
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(filepath.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            if (exif != null) {
                val orientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1
                )
                if (orientation != -1) {
                    // We only recognize a subset of orientation tag values.
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                        ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                        ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                    }
                }
            }
            return degree
        }


        // Rotates the bitmap by the specified degree.
        // If a new bitmap is created, the original bitmap is recycled.
        fun rotate(b: Bitmap?, degrees: Float): Bitmap? {
            var b = b
            if (degrees != 0.0F && b != null) {
                val m = Matrix()
                m.setRotate(
                    degrees, b.width.toFloat() / 2,
                    b.height.toFloat() / 2
                )
                try {
                    val b2 = Bitmap.createBitmap(
                        b, 0, 0, b.width,
                        b.height, m, true
                    )
                    if (b != b2) {
                        b.recycle()
                        b = b2
                    }
                } catch (ex: OutOfMemoryError) {
                    ex.printStackTrace()
                }
            }
            return b
        }

        //=================================================================
        //this is the byte stream that I upload.
        open fun getStreamByteFromImage(imageFile: File): ByteArray? {
            var photoBitmap = BitmapFactory.decodeFile(imageFile.getPath())
            val stream = ByteArrayOutputStream()
            val imageRotation: Int = getImageRotation(imageFile)
            if (imageRotation != 0) photoBitmap =
                getBitmapRotatedByDegree(photoBitmap, imageRotation)
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        }

        private fun getImageRotation(imageFile: File): Int {
            var exif: ExifInterface? = null
            var exifRotation = 0
            try {
                exif = ExifInterface(imageFile.path)
                exifRotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return if (exif == null) 0 else exifToDegrees(exifRotation)
        }

        private fun exifToDegrees(rotation: Int): Int {
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90) return 90 else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) return 180 else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) return 270
            return 0
        }

        private fun getBitmapRotatedByDegree(bitmap: Bitmap, rotationDegree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.preRotate(rotationDegree.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap? {
            val ei = image_absolute_path?.let { androidx.exifinterface.media.ExifInterface(it) }
            val orientation: Int =
                ei!!.getAttributeInt(
                    androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                )
            return when (orientation) {
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> rotate(
                    bitmap,
                    90f
                )
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> rotate(
                    bitmap,
                    180f
                )
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> rotate(
                    bitmap,
                    270f
                )
                androidx.exifinterface.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
                    bitmap,
                    true,
                    false
                )
                androidx.exifinterface.media.ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(
                    bitmap,
                    false,
                    true
                )
                else -> bitmap
            }
        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path =
                MediaStore.Images.Media.insertImage(
                    inContext.contentResolver,
                    inImage,
                    "Title",
                    null
                )
            return Uri.parse(path)


        }
        fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
            val matrix = Matrix()
            matrix.preScale(
                if (horizontal) -1.toFloat() else 1.toFloat(),
                if (vertical) -1.toFloat() else 1.toFloat()
            )
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        fun getBitmap(path: String?): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                val f = File(path)
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return bitmap
        }
    }



}