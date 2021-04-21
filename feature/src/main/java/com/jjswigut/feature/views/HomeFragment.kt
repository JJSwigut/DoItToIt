package com.jjswigut.feature.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjswigut.core.base.BaseFragment
import com.jjswigut.core.utils.State
import com.jjswigut.feature.adapters.ListAdapter
import com.jjswigut.feature.databinding.FragmentHomeBinding
import com.jjswigut.feature.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLists()
        setupRecyclers()
    }

    private fun setupRecyclers() {

        binding.listRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listRecycler.adapter = adapter


    }

    private fun observeLists() {
        viewModel.allLists.observe(viewLifecycleOwner, { result ->
            when (result) {
                is State.Loading -> Log.d(TAG, "observeLists: loading")
                is State.Success -> result.data.let { adapter.updateData(it) }
                is State.Failed -> Log.d(TAG, "observeLists: ${result.message}")
                else -> Log.d(TAG, "observeLists: dang")
            }
        })
    }

}