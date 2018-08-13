package com.dalydays.android.photogallery

import android.net.Uri
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr(private var apiKey: String) {

    companion object {
        private const val TAG = "FlickrFetchr"
    }

    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    fun fetchItems() {
        try {

            val url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString()
            val jsonString = getUrlString(url)
            Log.d(TAG, url)

            Log.i(TAG, "Received JSON: $jsonString")

            val jsonBody = JSONObject(jsonString)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException) {
            Log.e(TAG, "Failed to parse JSON", je)
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