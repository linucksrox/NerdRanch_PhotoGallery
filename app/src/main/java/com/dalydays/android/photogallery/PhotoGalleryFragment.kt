package com.dalydays.android.photogallery

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.IOException

class PhotoGalleryFragment : Fragment() {

    companion object {
        private val TAG = "PhotoGalleryFragment"

        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        val mPhotoRecyclerView = v.findViewById<RecyclerView>(R.id.photo_recycler_view)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        return v
    }

    class FetchItemsTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val result = FlickrFetchr().getUrlString("https://www.bignerdranch.com")

                Log.i(TAG, "Fetched contents of URL: $result")
            } catch (ioe: IOException) {
                Log.e(TAG, "Failed to fetch URL: $ioe")
            }
            return null
        }

    }
}