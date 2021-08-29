package com.thewyp.minimusic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thewyp.minimusic.data.entities.Song
import com.thewyp.minimusic.databinding.SwipeItemBinding
import javax.inject.Inject

class SwipeSongAdapter @Inject constructor() : RecyclerView.Adapter<SwipeSongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View, private val binding: SwipeItemBinding) : RecyclerView.ViewHolder(itemView) {

        fun bind(song: Song) {
            binding.apply {
                val text = "${song.title} - ${song.subtitle}"
                tvPrimary.text = text

                itemView.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(song)
                    }
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SwipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding.root, binding)
    }

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }
}