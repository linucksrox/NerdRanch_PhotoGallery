package com.dalydays.android.photogallery

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {
    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val outStream = ByteArrayOutputStream()
            val inStream = connection.inputStream

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + ": with " + urlSpec)
            }

            var bytesRead = 0
            val buffer = ByteArray(1024)

            bytesRead = inStream.read(buffer)
            while (bytesRead > 0) {
                bytesRead = inStream.read(buffer)
                outStream.write(buffer, 0, bytesRead)
            }
            outStream.close()
            return outStream.toByteArray()
        } finally {
            connection.disconnect()
        }
    }
}