package com.dalydays.android.photogallery

import com.google.gson.annotations.SerializedName

data class FlickrResult(var photos: GalleryPage)

data class GalleryPage(@SerializedName("photo") var photoList: ArrayList<GalleryItem>)