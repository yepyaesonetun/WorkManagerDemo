package com.padcmyanmar.padcx.workmanagerdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection

/**
 * Created by Ye Pyae Sone Tun
 * on 2020-02-15.
 */

class DownloadWorker(context: Context, params: WorkerParameters): Worker(context,params){
    override fun doWork(): Result {
        // Get the input data
        val urlString = inputData.getString("imageUrl")
        val url = stringToURL(urlString)

        // IMPORTANT - Put internet permission on manifest file
        var connection: HttpURLConnection? = null

        try {
            // Initialize a new http url connection
            connection = url?.openConnection() as HttpURLConnection

            // Connect the http url connection
            connection.connect()

            // Get the input stream from http url connection
            val inputStream = connection.getInputStream()

            // Initialize a new BufferedInputStream from InputStream
            val bufferedInputStream = BufferedInputStream(inputStream)

            // Convert BufferedInputStream to Bitmap object

            // Return the downloaded bitmap
            val bmp: Bitmap? = BitmapFactory.decodeStream(bufferedInputStream)
            val uri: Uri? = bmp?.saveToInternalStorage(applicationContext)

            Log.d("download","success")
            // Return the success with output data
            return Result.success(createOutputData(uri))

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("download",e.toString())

        } finally {
            // Disconnect the http url connection
            connection?.disconnect()
        }
        Log.d("download","failed")
        return Result.failure(createOutputData(null))
    }


    // Method to create output data
    private fun createOutputData(uri: Uri?): Data {
        return Data.Builder()
            .putString("uriString", uri.toString())
            .build()
    }
}