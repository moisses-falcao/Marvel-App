package com.example.marvelapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentListCharacterBinding
import com.example.marvelapp.ui.adapters.CharacterAdapter
import com.example.marvelapp.ui.base.BaseFragment
import com.example.marvelapp.ui.state.ResourceState
import com.example.marvelapp.util.hide
import com.example.marvelapp.util.show
import com.example.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ListCharacterFragment: BaseFragment <FragmentListCharacterBinding, ListCharacterViewModel>() {

    override val viewModel: ListCharacterViewModel by viewModels()
    private val characterAdapter by lazy {CharacterAdapter()}

    override fun onViewCreated(view: View, savedInstancesState: Bundle?){
        super.onViewCreated(view, savedInstancesState)
        setupRecyclerView()
        clickAdapter()
        collectObserver()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.list.collect{ resource ->
            when(resource){
                is ResourceState.Success ->{
                    resource.data?.let{values ->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results.toList()
                    }
                }
                is ResourceState.Error ->{
                    binding.progressCircular.hide()
                    resource.message?.let{message ->
                        toast(getString(R.string.an_error_ocurred))
                        Timber.tag("ListCharacterFragment").e("Error -> $message")
                    }
                }
                is ResourceState.Loading ->{
                    binding.progressCircular.show()
                }
                else ->{}
            }

        }
    }

    private fun clickAdapter() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = ListCharacterFragmentDirections
                .actionListCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListCharacterBinding = FragmentListCharacterBinding.inflate(inflater, container, false)


}
