package com.dalydays.android.photogallery

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
        FetchItemsTask().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        val mPhotoRecyclerView = v.findViewById<RecyclerView>(R.id.photo_recycler_view)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        return v
    }

    class FetchItemsTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            FlickrFetchr().fetchItems()
            return null
        }

    }
}