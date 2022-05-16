package com.thao.placesearch.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thao.placesearch.BuildConfig
import com.thao.placesearch.databinding.ItemPlaceBinding
import com.thao.placesearch.model.place.Place
import com.thao.placesearch.ui.viewmodel.PlaceViewModel
import com.thao.placesearch.utils.Constants

class PlacesAdapter(
    private val context: Context,
    private val items: List<Place>,
    private var placeViewModel: PlaceViewModel,
    private var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.apply {
                viewModel = placeViewModel
                item.getPhotoReference()?.let {
                    var photoUrl =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=${Constants.MAX_WIDTH_THUMBNAIL}&maxheight=${Constants.MAX_HEIGHT_THUMBNAIL}&photoreference=$it&key=${
                            BuildConfig.PLACES_API_KEY
                        }"

                    Glide.with(context).load(photoUrl)
                        .skipMemoryCache(true)
                        .into(imvThumbnail)
                }
                place = item
                executePendingBindings()
                layoutPlaceItem.rootView.setOnClickListener {
                    onItemClickListener?.onItemClick(item)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Place)
    }

}