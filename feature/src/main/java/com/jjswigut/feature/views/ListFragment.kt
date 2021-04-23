package com.jjswigut.feature.views

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jjswigut.core.base.BaseBottomSheetDialogFragment
import com.jjswigut.core.utils.State
import com.jjswigut.feature.adapters.TaskAdapter
import com.jjswigut.feature.databinding.FragmentListBinding
import com.jjswigut.feature.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ListFragment : BaseBottomSheetDialogFragment<ListViewModel>() {

    override val viewModel: ListViewModel by activityViewModels()

    private lateinit var adapter: TaskAdapter

    private val args: ListFragmentArgs by navArgs()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TaskAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupRecyclers()
        setupDelete(args.listId)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
        }).attachToRecyclerView(binding.taskRecycler)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is ListViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
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
                    binding.nameView.text = it[0].list.name.toEditable()
                    viewModel.listId = args.listId
                    adapter.updateData(it[0].tasks)
                }
                is State.Failed -> Log.d(TAG, "observeListOfTasks: ${result.message}")
                else -> Log.d(TAG, "observeListOfTasks: dang")
            }
        })
    }

    private fun setupDelete(listId: Long) {
        binding.deleteButton.setOnClickListener {
            Log.d(TAG, "setupDelete: clicked")
            showConfirmationDialog(listId)
        }
    }

    private fun showConfirmationDialog(listId: Long) {
        with(AlertDialog.Builder(context)) {
            setMessage("Are you sure?")
            setNegativeButton("Nope!") { dialog, _ -> dialog.dismiss() }
            setPositiveButton("Yup!") { _, _ ->
                viewModel.deleteList(listId)
                findNavController().navigateUp()
            }
            create()
            show()
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


}