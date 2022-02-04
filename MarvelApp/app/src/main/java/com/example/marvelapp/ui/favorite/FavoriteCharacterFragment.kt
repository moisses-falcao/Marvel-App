package com.example.marvelapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentFavoritCharacterBinding
import com.example.marvelapp.ui.adapters.CharacterAdapter
import com.example.marvelapp.ui.base.BaseFragment
import com.example.marvelapp.ui.details.DetailsCharacterFragment
import com.example.marvelapp.ui.state.ResourceState
import com.example.marvelapp.util.hide
import com.example.marvelapp.util.show
import com.example.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCharacterFragment: BaseFragment<FragmentFavoritCharacterBinding, FavoriteCharacterViewModel>() {

    override val viewModel: FavoriteCharacterViewModel by viewModels()
    private val characterAdapter: CharacterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        clickAdapter()
        observer()
    }

    private fun observer() = lifecycleScope.launch {
        viewModel.favorits.collect {
            when(it){
                is ResourceState.Success ->{
                    it.data?.let{
                        binding.tvEmptyList.hide()
                        characterAdapter.characters = it.toList()
                    }
                }
                is ResourceState.Empty ->{
                    binding.tvEmptyList.show()
                }
                else ->{}
            }
        }
    }

    private fun clickAdapter() {
        characterAdapter.setOnClickListener {
            val action = FavoriteCharacterFragmentDirections
                .actionFavoriteCharacterFragmentToDetailsCharacterFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvFavoritCharacter.adapter = characterAdapter
        rvFavoritCharacter.layoutManager = LinearLayoutManager(context)

        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvFavoritCharacter)
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback{
        return object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)
                viewModel.delete(character).also {
                    toast(getString(R.string.messege_delete_character))
                }
            }

        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritCharacterBinding = FragmentFavoritCharacterBinding.inflate(inflater, container, false)
}