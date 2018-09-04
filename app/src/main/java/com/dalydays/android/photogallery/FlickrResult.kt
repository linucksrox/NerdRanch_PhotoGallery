package com.dalydays.android.photogallery

import com.google.gson.annotations.SerializedName

data class FlickrResult(var photos: GalleryPage,
                        var stat: String)

data class GalleryPage(var page: Int,
                        var pages: Int,
                        var perpage: Int,
                        var total: Int,
                        @SerializedName("photo")
                        var photoList: ArrayList<GalleryItem>)