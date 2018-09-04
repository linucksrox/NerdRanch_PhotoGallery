package com.dalydays.android.photogallery

data class GalleryItem(var id: String,
                       var owner: String,
                       var secret: String,
                       var server: String,
                       var farm: Int,
                       var title: String,
                       var ispublic: Int,
                       var isfriend: Int,
                       var isfamily: Int,
                       var url_s: String,
                       var height_s: String,
                       var width_s: String) {

    override fun toString(): String {
        return title
    }
}