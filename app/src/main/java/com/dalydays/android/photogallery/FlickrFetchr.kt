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

    fun fetchItems(): List<GalleryItem> {

        val items = ArrayList<GalleryItem>()

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
            parseItems(items, jsonBody)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException) {
            Log.e(TAG, "Failed to parse JSON", je)
        }

        return items
    }

    @Throws(IOException::class, JSONException::class)
    fun parseItems(items: MutableList<GalleryItem>, jsonBody: JSONObject) {
        val photosJsonObject = jsonBody.getJSONObject("photos")
        val photosJsonArray = photosJsonObject.getJSONArray("photo")

        for (i in 0..photosJsonArray.length()) {
            val photoJsonObject = photosJsonArray.getJSONObject(i)

            val item = GalleryItem(photoJsonObject.getString("id"), photoJsonObject.getString("title"))

            if (!photosJsonObject.has("url_s")) {
                continue
            }

            item.url = photosJsonObject.getString("url_s")
            items.add(item)
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