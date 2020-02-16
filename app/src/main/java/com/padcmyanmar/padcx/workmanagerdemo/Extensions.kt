package com.padcmyanmar.padcx.workmanagerdemo

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by Ye Pyae Sone Tun
 * on 2020-02-15.
 */

// Extension function to save bitmap in internal storage
fun Bitmap.saveToInternalStorage(context: Context): Uri {
    // Get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // Initializing a new file
    // The bellow line return a directory in internal storage
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)


    // Create a file to save the image
    file = File(file, "${UUID.randomUUID()}.png")

    try {
        // Get the file output stream
        val stream: OutputStream = FileOutputStream(file)

        // Compress bitmap
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)

        // Flush the stream
        stream.flush()

        // Close stream
        stream.close()
    } catch (e: IOException){ // Catch the exception
        e.printStackTrace()
    }

    // Return the saved image uri
    return Uri.parse(file.absolutePath)
}


// Custom method to convert string to url
fun stringToURL(urlString: String?): URL? {
    urlString?.let {
        try {
            return URL(urlString)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    return null
}


fun Context.toast(message:String)=
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()