package com.dalydays.android.photogallery

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

class PhotoGalleryFragment : Fragment() {

    companion object {
        private const val TAG = "PhotoGalleryFragment"

        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }

    private lateinit var mPhotoRecyclerView: RecyclerView
    var mItems = ArrayList<GalleryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // Store the api key in a private.xml values file and don't track it in git
        val apiKey = getString(R.string.api_key)
        FetchItemsTask(apiKey).execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        // Add onScrollListener to check if we're at the end of the list so we can load the next page
        mPhotoRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val lastItemIndex = recyclerView?.layoutManager?.itemCount!! - 1

                if (recyclerView.layoutManager?.getChildAt(lastItemIndex)?.visibility == View.VISIBLE) {
                    FetchItemsTask(getString(R.string.api_key)).execute()

                    // TODO: Figure out which page to request, and don't request a page if we know it doesn't exist

                }
            }
        })

        setupAdapter()

        return v
    }

    private fun setupAdapter() {
        if (isAdded) {
            mPhotoRecyclerView.adapter = PhotoAdapter(mItems)
        }
    }

    private inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTitleTextView = itemView as TextView

        fun bindGalleryItem(item: GalleryItem) {
            mTitleTextView.text = item.toString()
        }
    }

    private inner class PhotoAdapter(galleryItems: List<GalleryItem>) : RecyclerView.Adapter<PhotoHolder>() {
        private val mGalleryItems = galleryItems

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(activity)
            return PhotoHolder(textView)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = mGalleryItems[position]
            holder.bindGalleryItem(galleryItem)
        }

        override fun getItemCount(): Int {
            return mGalleryItems.size
        }
    }

    private inner class FetchItemsTask(private val apiKey: String) : AsyncTask<Void, Void, List<GalleryItem>>() {

        override fun doInBackground(vararg params: Void): List<GalleryItem> {
            return FlickrFetchr(apiKey).fetchItems()
        }

        override fun onPostExecute(result: List<GalleryItem>) {
            mItems = result as ArrayList<GalleryItem>
            setupAdapter()
        }

    }
}