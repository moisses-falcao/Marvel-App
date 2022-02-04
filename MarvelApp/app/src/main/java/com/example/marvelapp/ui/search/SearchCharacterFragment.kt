package com.example.marvelapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentSearchCharacterBinding
import com.example.marvelapp.ui.adapters.CharacterAdapter
import com.example.marvelapp.ui.base.BaseFragment
import com.example.marvelapp.ui.state.ResourceState
import com.example.marvelapp.util.Constants.DEFAULT_QUERY
import com.example.marvelapp.util.Constants.LAST_SEARCH_QUERY
import com.example.marvelapp.util.hide
import com.example.marvelapp.util.show
import com.example.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchCharacterFragment: BaseFragment<FragmentSearchCharacterBinding, SearchCharacterViewModel>() {

    override val viewModel: SearchCharacterViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        clickAdapter()

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY)?: DEFAULT_QUERY
        searchInit(query)
        collectObserver()

    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.searchCharacter.collect { result ->
            when(result){
                is ResourceState.Success ->{
                    binding.progressbarSearch.hide()
                    result.data?.let {
                        characterAdapter.characters = it.data.results.toList()
                    }
                }
                is ResourceState.Error ->{
                    binding.progressbarSearch.hide()
                    result.message?.let { message ->
                        Timber.tag("SearchCharacterFragment").e("Error -> $message")
                        toast(getString(R.string.an_error_ocurred))
                    }

                }
                is ResourceState.Loading -> {
                    binding.progressbarSearch.show()
                }
                else -> {}
            }
        }
    }

    private fun searchInit(query: String) = with(binding) {
        edSearchCharacter.setText(query)
        edSearchCharacter.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO){
                updateCharacterList()
                true
            }else{
                false
            }

        }

        edSearchCharacter.setOnKeyListener { _, i, event ->
            if (event.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                updateCharacterList()
                true
            }else{
                false
            }
        }

    }

    private fun updateCharacterList() = with(binding) {
        edSearchCharacter.editableText.trim().let {
            if (it.isNotEmpty()){
                searchQuery(it.toString())
            }
        }
    }

    private fun searchQuery(query: String) = lifecycleScope.launch {
        viewModel.fetch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY,
        binding.edSearchCharacter.editableText.trim().toString())
    }

    private fun clickAdapter(){
        characterAdapter.setOnClickListener { characterModel ->
            val action = SearchCharacterFragmentDirections
                .actionSearchCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() = with(binding){
        rvSearchCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchCharacterBinding  = FragmentSearchCharacterBinding.inflate(inflater, container, false)


}