package com.androidfiletransfer.files

import android.webkit.MimeTypeMap
import java.io.File

class MimeType {

    fun get(file : File) : String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

}