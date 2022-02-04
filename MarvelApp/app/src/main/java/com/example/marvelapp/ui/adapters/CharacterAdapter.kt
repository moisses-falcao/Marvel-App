package com.example.marvelapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvelapp.R
import com.example.marvelapp.data.model.character.CharacterModel
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.util.limitDescription

class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<CharacterModel>(){
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name &&
                    oldItem.description == newItem.description &&
                    oldItem.thumbnailModel.extension == newItem.thumbnailModel.extension &&
                    oldItem.thumbnailModel.path == newItem.thumbnailModel.path
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var characters: List<CharacterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.binding.apply{
            tvNameCharacter.text = character.name
            if (character.description == ""){
                tvDescriptionCharacter.text = holder.itemView.context.getString(R.string.tv_item_description_empty)
            } else{
                tvDescriptionCharacter.text = character.description.limitDescription(100)
            }

            Glide.with(holder.itemView.context)
                .load(character.thumbnailModel.path + "." + character.thumbnailModel.extension)
                .into(imgCharacter)
        }
        holder.itemView.setOnClickListener{
            onItemClickListener?.let{
                it(character)
            }
        }

    }

    override fun getItemCount(): Int {
        return characters.size
    }

    inner class CharacterViewHolder(val binding : ItemCharacterBinding): RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    fun setOnClickListener(Listener: (CharacterModel) -> Unit){
        onItemClickListener = Listener
    }

    fun getCharacterPosition(position: Int): CharacterModel {
        return characters[position]
    }
}