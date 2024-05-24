package com.example.task

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.task.pexelsapi.PexelsApiService
import com.example.task.pexelsapi.dataclases.Photo
import com.example.task.room.AppDatabase
import com.example.task.room.entities.RecentImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var photographerTextView: TextView
    private lateinit var addToBookmarksButton: ImageButton
    private lateinit var db: AppDatabase

    private lateinit var photographer: String
    private lateinit var imageID: String
    private lateinit var imageUrl: String


    private var API_KEY = "api_key"
    private fun saveImageToDatabase(imageId: String, photographer: String, url: String) {
        lifecycleScope.launch {
            val recentImage = RecentImage(imageId = imageId, photographer = photographer, imageUrl = url )
            db.recentImageDao().insert(recentImage)
        }
    }


    private fun getPhoto(id: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pexelsApiService = retrofit.create(PexelsApiService::class.java)
        val call = pexelsApiService.getPhoto(id, API_KEY)

        call.enqueue(object : Callback<Photo> {
            override fun onResponse(call: Call<Photo>, response: Response<Photo>) {
                if (response.isSuccessful) {
                    val photo = response.body()
                    Glide.with(this@DetailsActivity)
                        .load(photo?.src?.original)
                        .into(imageView)
                    imageUrl = photo?.src?.original ?: ""
                    photographer = photo?.photographer ?: ""
                    photographerTextView.text = "$photographer"
                } else {

                }
            }

            override fun onFailure(call: Call<Photo>, t: Throwable) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        imageView = findViewById(R.id.detailsImageView)
        backButton = findViewById(R.id.backButton)
        photographerTextView = findViewById(R.id.nameTextView)
        addToBookmarksButton = findViewById(R.id.addToBookmarksButton)

        db = AppDatabase.getDatabase(this)

        imageID =  intent.getStringExtra("imageId") ?: ""
        getPhoto(imageID)

        backButton.setOnClickListener {
            onBackPressed()
        }
        addToBookmarksButton.setOnClickListener{
            saveImageToDatabase(imageID, photographer, imageUrl)
        }
    }
}