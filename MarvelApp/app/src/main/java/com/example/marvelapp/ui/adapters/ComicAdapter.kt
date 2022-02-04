package com.example.marvelapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvelapp.R
import com.example.marvelapp.data.model.character.CharacterModel
import com.example.marvelapp.data.model.comic.ComicModel
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.databinding.ItemComicBinding
import com.example.marvelapp.util.limitDescription

class ComicAdapter: RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<ComicModel>(){
        override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title &&
                    oldItem.description == newItem.description &&
                    oldItem.thumbnailModel.extension == newItem.thumbnailModel.extension &&
                    oldItem.thumbnailModel.path == newItem.thumbnailModel.path
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var comics: List<ComicModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ItemComicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comics[position]
        holder.binding.apply{
            tvNameComic.text = comic.title
            tvDescriptionComic.text = comic.description

            Glide.with(holder.itemView.context)
                .load(comic.thumbnailModel.path + "." + comic.thumbnailModel.extension)
                .into(imgComic)
        }
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    inner class ComicViewHolder(val binding : ItemComicBinding): RecyclerView.ViewHolder(binding.root)
}