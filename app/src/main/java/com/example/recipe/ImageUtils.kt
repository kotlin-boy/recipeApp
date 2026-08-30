package com.example.recipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.graphics.Matrix
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider

//画像の回転
object ImageUtils {

    fun rotateImage(
        context: Context,
        imageUri: String
    ): String {

        val uri = Uri.parse(imageUri)

        val bitmap: Bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source =
                    ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            }

        //90度回転命令
        val matrix = Matrix()
        matrix.postRotate(90f)

        //回転画像の作成
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

        //回転画像の保存
        val file = File(
            context.cacheDir,
            "rotated_image_${System.currentTimeMillis()}.jpg"
        )
        FileOutputStream(file).use { outputStream ->
            rotatedBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                outputStream
            )
        }

        //メモリ開放
        bitmap.recycle()
        rotatedBitmap.recycle()

        //uriの取得
        val newUri = Uri.fromFile(file)
        return newUri.toString()
    }

    //撮影画像のURI作成
    fun createImageUri(
        context: Context
    ): Uri {

        val file = File(
            context.cacheDir,
            "camera_image_${System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}