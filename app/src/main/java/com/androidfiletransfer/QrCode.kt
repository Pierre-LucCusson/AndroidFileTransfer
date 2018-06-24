package com.androidfiletransfer

import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

const val WIDTH = 800

class QrCode {

    private val encryptedMessage : String
    private val qrImage : Bitmap?

    constructor(encryptedMessage : String) {
        this.encryptedMessage = encryptedMessage
        qrImage = getQrImage()
    }

    private fun getQrImage() : Bitmap? {

        try {
            val bitMatrix : BitMatrix

            bitMatrix = MultiFormatWriter().encode(encryptedMessage, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null)

            val w = bitMatrix.width
            val h = bitMatrix.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) BLACK else WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h)
            return bitmap

        }
        catch (e : Exception) {
            return null
        }
    }

    fun getImage() : Bitmap? {
        return qrImage
    }

}