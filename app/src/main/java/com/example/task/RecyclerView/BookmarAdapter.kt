package com.example.task.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task.R

class BookmarksAdapter(
    private var imageUrls: List<String>,
    private var authors: List<String>
) : RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder>() {

    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_item, parent, false) // Use your item layout
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(imageUrls[position])
            .into(holder.imageView)

        holder.authorTextView.text = authors[position]
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
    fun updateData(newImageUrls: List<String>, newAuthors: List<String>) {
        imageUrls = newImageUrls
        authors = newAuthors
        notifyDataSetChanged()
    }

}