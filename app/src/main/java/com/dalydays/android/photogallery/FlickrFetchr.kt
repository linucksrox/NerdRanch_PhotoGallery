package com.dalydays.android.photogallery

import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {

    companion object {
        private val TAG = "FlickrFetchr"
        // Store the api key in a private.xml values file and don't track it in git
        private const val API_KEY = R.string.api_key.toString()
    }

    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    fun fetchItems() {
        try {
            val url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString()
            val jsonString = getUrlString(url)

            Log.i(TAG, "Received JSON: $jsonString")
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        }
    }

    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val outStream = ByteArrayOutputStream()
            val inStream = connection.inputStream

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + ": with " + urlSpec)
            }

            var bytesRead: Int
            val buffer = ByteArray(1024)

            bytesRead = inStream.read(buffer)
            while (bytesRead > 0) {
                outStream.write(buffer, 0, bytesRead)
                bytesRead = inStream.read(buffer)
            }
            outStream.close()

            return outStream.toByteArray()
        } finally {
            connection.disconnect()
        }
    }
}