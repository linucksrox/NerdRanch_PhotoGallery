package com.dalydays.android.photogallery

import android.support.v4.app.Fragment

class PhotoGalleryActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return PhotoGalleryFragment.newInstance()
    }
}
