package com.jjswigut.feature.lists

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jjswigut.core.base.BaseFragment
import com.jjswigut.core.utils.State
import com.jjswigut.data.local.CardAction
import com.jjswigut.data.local.SwipeEvent
import com.jjswigut.feature.databinding.FragmentListsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ListsFragment : BaseFragment<ListsViewModel>() {

    override val viewModel: ListsViewModel by activityViewModels()

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListAdapter(::handleClick)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        observeLists()
        setupRecyclers()
        setUpItemTouchHelper().attachToRecyclerView(binding.listRecycler)
        setUpSwipeEvent()
    }

    private fun setupRecyclers() {
        with(binding) {
            listRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            listRecycler.adapter = adapter
        }
    }

    private fun setUpToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar
            .setupWithNavController(navController, appBarConfiguration)
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

    private fun handleClick(click: CardAction) {
        when (click) {
            is CardAction.ListCardClicked -> {
                viewModel.navigate(ListsFragmentDirections.homeToList(click.list.listId))
            }
            is CardAction.AddCardClicked -> {
                viewModel.navigate(ListsFragmentDirections.homeToAdd(click.type.ordinal))
            }
        }
    }

    private fun setUpItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val list = adapter.elements[viewHolder.adapterPosition]
                viewModel.onListSwiped(list)
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is ListAdapter.AddViewHolder) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        })
    }

    private fun setUpSwipeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.listsEvent.collect { swipe ->
                when (swipe) {
                    is SwipeEvent.ShowUndoDeleteListMessage -> {
                        Snackbar.make(
                            requireView(),
                            "List deleted",
                            BaseTransientBottomBar.LENGTH_LONG
                        )
                            .setAction("UNDO") {
                                adapter.notifyDataSetChanged()
                            }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    super.onDismissed(transientBottomBar, event)
                                    viewModel.deleteList(swipe.list.listId)
                                }
                            }).show()

                    }
                }
            }
        }
    }
}