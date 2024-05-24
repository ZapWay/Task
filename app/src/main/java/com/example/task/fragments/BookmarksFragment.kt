package com.example.task.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.RecyclerView.BookmarksAdapter
import com.example.task.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class BookmarksFragment : Fragment() {
    private lateinit var bookmarksRecyclerView: RecyclerView
    private lateinit var bookmarksAdapter: BookmarksAdapter

    var imageIds = mutableListOf<String>();
    var authors = mutableListOf<String>();
    var imageUrls = mutableListOf<String>()

    private fun getBookmarks() {
        val db = AppDatabase.getDatabase(requireContext())
        val recentImageDao = db.recentImageDao()
        CoroutineScope(Dispatchers.IO).launch {
            val recentImages = recentImageDao.getAllImages()
            imageIds.addAll(recentImages.map { it.imageId })
            authors.addAll(recentImages.map { it.photographer })
            imageUrls.addAll(recentImages.map { it.imageUrl})

            withContext(Dispatchers.Main) {
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBookmarks()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }
}