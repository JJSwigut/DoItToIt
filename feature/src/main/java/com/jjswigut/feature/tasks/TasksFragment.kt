package com.jjswigut.feature.tasks

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jjswigut.core.base.BaseBottomSheetDialogFragment
import com.jjswigut.core.utils.State
import com.jjswigut.data.local.CardAction
import com.jjswigut.data.local.SwipeEvent
import com.jjswigut.feature.R
import com.jjswigut.feature.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TasksFragment : BaseBottomSheetDialogFragment<TasksVIewModel>() {

    override val viewModel: TasksVIewModel by activityViewModels()

    private lateinit var adapter: TaskAdapter

    private val args: TasksFragmentArgs by navArgs()

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TaskAdapter(::handleClick)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setupObservers()
        setupRecyclers()
        setUpItemTouchHelper().attachToRecyclerView(binding.taskRecycler)
        setUpSwipeEvent()

    }

    private fun setupRecyclers() {
        with(binding) {
            taskRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            taskRecycler.adapter = adapter
        }
    }

    private fun setupObservers() {
        viewModel.getListOfTasks(args.listId).observe(viewLifecycleOwner, { result ->
            when (result) {
                is State.Loading -> Log.d(TAG, "setupObservers: loading")
                is State.Success -> result.data.let {
                    binding.nameView.text = it[0].list.name
                    adapter.updateData(it[0].tasks)
                }
                is State.Failed -> Log.d(TAG, "observeListOfTasks: ${result.message}")
                else -> Log.d(TAG, "observeListOfTasks: dang")
            }
        })
    }

    private fun setUpToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar
            .setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deleteIcon -> setupDelete(args.listId)
            }
            true
        }
    }

    private fun setupDelete(listId: Long) {
        showConfirmationDialog(listId)
    }

    private fun showConfirmationDialog(listId: Long) {
        with(AlertDialog.Builder(context)) {
            setMessage("Are you sure you want to delete this list?")
            setNegativeButton("Nope!") { dialog, _ -> dialog.dismiss() }
            setPositiveButton("Yup!") { _, _ ->
                viewModel.deleteList(listId)
                findNavController().navigateUp()
            }
            create()
            show()
        }
    }

    private fun handleClick(click: CardAction) {
        when (click) {
            is CardAction.AddCardClicked -> {
                viewModel.navigate(
                    TasksFragmentDirections.listToAdd(
                        click.type.ordinal,
                        args.listId
                    )
                )
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
                val task = adapter.elements[viewHolder.adapterPosition]
                viewModel.onTaskSwiped(task)
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is TaskAdapter.AddViewHolder) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        })
    }

    private fun setUpSwipeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is SwipeEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Task deleted",
                            BaseTransientBottomBar.LENGTH_LONG
                        )
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                }
            }
        }
    }

}