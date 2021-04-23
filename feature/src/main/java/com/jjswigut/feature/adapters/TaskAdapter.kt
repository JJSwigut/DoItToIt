package com.jjswigut.feature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.core.utils.ListDiffCallback
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.feature.R
import com.jjswigut.feature.databinding.ItemAddtaskBinding
import com.jjswigut.feature.databinding.ItemTaskBinding
import com.jjswigut.feature.viewmodels.ListViewModel
import com.jjswigut.feature.views.ListFragmentDirections

class TaskAdapter(private val viewModel: ListViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var elements = ArrayList<TaskEntity>()
    fun updateData(newData: List<TaskEntity>) {

        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = elements.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            elements.size -> R.layout.item_addtask
            else -> R.layout.item_task

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.item_task -> TaskViewHolder(
                binding = ItemTaskBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            R.layout.item_addtask -> AddViewHolder(
                binding = ItemAddtaskBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent, false
                )
            )
            else -> throw Exception("Nope")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_task -> (holder as TaskViewHolder)
                .bind(elements[position])
            R.layout.item_addtask -> (holder as AddViewHolder)
                .bind()
        }
    }

    inner class TaskViewHolder(
        binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private val nameView = binding.taskNameView

        fun bind(item: TaskEntity) {
            nameView.text = item.body
        }
    }

    inner class AddViewHolder(
        binding: ItemAddtaskBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private val addCard = binding.addTaskCard

        fun bind() {
            addCard.setOnClickListener {
                viewModel.navigate(ListFragmentDirections.listToAdd(isTask, viewModel.listId))
            }
        }
    }

    companion object {
        const val isTask = 1
    }
}