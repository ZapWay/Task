package com.example.task.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

class HomeFragment : Fragment() {

    private var API_KEY = "my_api_key"


    private lateinit var binding: FragmentHomeBinding
    private lateinit var searchBar: SearchView
    private lateinit var recycler: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var wifiImage: ImageView
    private lateinit var tryAgainButton: TextView
    private lateinit var exploreButton: TextView
    private lateinit var exploreText: TextView
    private lateinit var chipListView: HorizontalScrollView

    private lateinit var chip1: Chip
    private lateinit var chip2: Chip
    private lateinit var chip3: Chip
    private lateinit var chip4: Chip
    private lateinit var chip5: Chip
    private lateinit var chip6: Chip
    private lateinit var chip7: Chip

    private val collectionIDs = mutableListOf<String>()
    private val imagesIDs = mutableListOf<String>()

    private fun setChipListener(chip: Chip, id: Int) {
        chip.setOnClickListener {
            run {
                getImagesByCollection(collectionIDs[id], 1, 30)
            }
        }
    }

    private fun setChipNames(list: List<String>?) {
        chip1.text = list!![0]
        chip2.text = list[1]
        chip3.text = list[2]
        chip4.text = list[3]
        chip5.text = list[4]
        chip6.text = list[5]
        chip7.text = list[6]
    }

    private fun initChipListeners() {
        setChipListener(chip1, 0)
        setChipListener(chip2, 1)
        setChipListener(chip3, 2)
        setChipListener(chip4, 3)
        setChipListener(chip5, 4)
        setChipListener(chip6, 5)
        setChipListener(chip7, 6)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initListeners() {
        tryAgainButton.setOnClickListener { _ ->
            run {
                getCuratedImages()
                getChipNames()
            }
        }
        exploreButton.setOnClickListener { _ ->
            run {
                getCuratedImages()
                getChipNames()
            }
        }
    }
    private fun getPhoto(position: Int) {
        val id = imagesIDs[position]
        Log.d("RecyclerViewClick", "Clicked item ID: $id")
    }

    private fun initUIComponents() {
        recycler = binding.recyclerView
        imageAdapter = ImageAdapter()
        imageAdapter.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                getPhoto(position)
            }
        })

        recycler.adapter = imageAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        searchBar = binding.searchView

        wifiImage = binding.wifiImage
        tryAgainButton = binding.tryAgainButton
        exploreText = binding.exploreText
        exploreButton = binding.exploreTextButton
        tryAgainButton = binding.tryAgainButton
        chipListView = binding.itemScrollView

        chip1 = binding.chip
        chip2 = binding.chip2
        chip3 = binding.chip3
        chip4 = binding.chip4
        chip5 = binding.chip5
        chip6 = binding.chip6
        chip7 = binding.chip7
    }

    private fun getImagesByCollection(collectionID: String, page: Int, perPage: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(PexelsApiService::class.java)
        apiService.getCollectionMedia(API_KEY, collectionID, page, perPage)
            .enqueue(object : Callback<CollectionMediaResponse> {
                override fun onResponse(
                    call: Call<CollectionMediaResponse>,
                    response: Response<CollectionMediaResponse>
                ) {
                    if (response.isSuccessful) {
                        val imageUrls = response.body()?.media?.map {
                            if (it.type == "Photo") it.src.original else it.image
                        }
                        imagesIDs.clear()
                        imagesIDs.addAll(response.body()?.media?.map { it.id.toString() } ?: emptyList())
                        if (imageUrls != null) {
                            if (recycler.visibility == View.GONE) {
                                recycler.visibility = View.VISIBLE
                            }
                            if (wifiImage.visibility == View.VISIBLE) {
                                wifiImage.visibility = View.GONE
                            }
                            if (tryAgainButton.visibility == View.VISIBLE) {
                                tryAgainButton.visibility = View.GONE
                            }
                            if (exploreText.visibility == View.VISIBLE) {
                                exploreText.visibility = View.GONE
                            }
                            if (exploreText.visibility == View.VISIBLE) {
                                exploreText.visibility = View.GONE
                            }
                            if (chipListView.visibility == View.GONE) {
                                chipListView.visibility = View.VISIBLE
                            }
                            imageAdapter.updateImages(imageUrls)
                        } else {
                            recycler.visibility = View.GONE
                            exploreText.visibility = View.VISIBLE
                            exploreButton.visibility = View.VISIBLE
                        }
                    } else {
                        recycler.visibility = View.GONE
                        chipListView.visibility = View.GONE
                        wifiImage.visibility = View.VISIBLE
                        tryAgainButton.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<CollectionMediaResponse>, t: Throwable) {
                    recycler.visibility = View.GONE
                    chipListView.visibility = View.GONE
                    wifiImage.visibility = View.VISIBLE
                    tryAgainButton.visibility = View.VISIBLE
                }
            })
    }

    private fun getCuratedImages() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(PexelsApiService::class.java)
        apiService.getCuratedImages(API_KEY, 1, 30)
            .enqueue(object : Callback<PexelsResponse> {
                override fun onResponse(
                    call: Call<PexelsResponse>,
                    response: Response<PexelsResponse>
                ) {
                    if (response.isSuccessful) {
                        val imageUrls = response.body()?.photos?.map { it.src.original }
                        if (imageUrls != null) {
                            imagesIDs.clear()
                            imagesIDs.addAll(response.body()?.photos?.map { it.id.toString() } ?: emptyList())

                            if (recycler.visibility == View.GONE) {
                                recycler.visibility = View.VISIBLE
                            }
                            if (wifiImage.visibility == View.VISIBLE) {
                                wifiImage.visibility = View.GONE
                            }
                            if (tryAgainButton.visibility == View.VISIBLE) {
                                tryAgainButton.visibility = View.GONE
                            }
                            if (exploreText.visibility == View.VISIBLE) {
                                exploreText.visibility = View.GONE
                            }
                            if (chipListView.visibility == View.GONE) {
                                chipListView.visibility = View.VISIBLE
                            }
                            imageAdapter.updateImages(imageUrls)
                        } else {
                            recycler.visibility = View.GONE
                            exploreText.visibility = View.VISIBLE
                            exploreButton.visibility = View.VISIBLE
                        }
                    } else {
                        recycler.visibility = View.GONE
                        chipListView.visibility = View.GONE
                        wifiImage.visibility = View.VISIBLE
                        tryAgainButton.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<PexelsResponse>, t: Throwable) {
                    recycler.visibility = View.GONE
                    chipListView.visibility = View.GONE
                    wifiImage.visibility = View.VISIBLE
                    tryAgainButton.visibility = View.VISIBLE
                }
            })
    }

    private fun getChipNames() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(PexelsApiService::class.java)

        apiService.getFeaturedCollections(
            API_KEY,
            1,
            7
        ).enqueue(object : Callback<FeaturedCollectionsResponse> {
            override fun onResponse(
                call: Call<FeaturedCollectionsResponse>,
                response: Response<FeaturedCollectionsResponse>
            ) {
                if (response.isSuccessful) {
                    val collections = response.body()?.collections
                    if (collections != null) {
                        val collectionNames = collections.map { it.title }
                        collectionIDs.clear()
                        collectionIDs.addAll(collections.map { it.id })

                        setChipNames(collectionNames)
                        initChipListeners()
                    } else {

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<FeaturedCollectionsResponse>, t: Throwable) {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents()
        initListeners()
        getCuratedImages()
        getChipNames()
    }

    data class Image(
        val imageUrl: String,
    )

    interface PexelsApiService {
        @GET("curated")
        fun getCuratedImages(
            @Header("Authorization") apiKey: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
        ): Call<PexelsResponse>

        @GET("collections/featured")
        fun getFeaturedCollections(
            @Header("Authorization") apiKey: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
        ): Call<FeaturedCollectionsResponse>

        @GET("collections/{id}")
        fun getCollectionMedia(
            @Header("Authorization") apiKey: String,
            @Path("id") id: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
        ): Call<CollectionMediaResponse>
    }

    data class FeaturedCollectionsResponse(
        val collections: List<FeaturedCollection>
    )

    data class FeaturedCollection(
        val id: String,
        val title: String,
    )

    data class PexelsResponse(
        val photos: List<Photo>
    )

    data class Photo(
        val id: Int,
        val src: Src
    )

    data class CollectionMediaResponse(
        val id: String,
        val media: List<MediaItem>
    )

    data class MediaItem(
        val id: Int,
        val type: String,
        val src: Src,
        val image: String
    )

    data class Src(
        val original: String
    )

    inner class ImageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

        private val images = mutableListOf<String>()
        private var itemClickListener: ItemClickListener? = null
        fun updateImages(newImages: List<String>) {
            images.clear()
            images.addAll(newImages)
            notifyDataSetChanged()
        }
        fun setItemClickListener(listener: ItemClickListener) {
            this.itemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
            return ImageViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageUrl = images[position]
            Glide.with(holder.itemView.context).load(imageUrl).into(holder.imageView)
            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(position)
            }
        }

        override fun getItemCount(): Int {
            return images.size
        }

    }
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}