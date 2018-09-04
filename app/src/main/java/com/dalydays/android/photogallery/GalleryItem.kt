package com.dalydays.android.photogallery

data class GalleryItem(var id: String,
                       var title: String,
                       var url_s: String) {

    override fun toString(): String {
        return title
    }
}