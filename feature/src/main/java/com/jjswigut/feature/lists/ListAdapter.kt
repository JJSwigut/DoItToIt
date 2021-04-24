package com.jjswigut.feature.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.core.utils.ListDiffCallback
import com.jjswigut.data.local.AddType
import com.jjswigut.data.local.CardAction
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.feature.R
import com.jjswigut.feature.databinding.ItemAddlistBinding
import com.jjswigut.feature.databinding.ItemListBinding

class ListAdapter(private val clickHandler: ClickHandler) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var elements = ArrayList<ListEntity>()

    fun updateData(newData: List<ListEntity>) {

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
            elements.size -> R.layout.item_addlist
            else -> R.layout.item_list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.item_list -> ListViewHolder(
                binding = ItemListBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            R.layout.item_addlist -> AddViewHolder(
                binding = ItemAddlistBinding.inflate(
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
            R.layout.item_list -> (holder as ListViewHolder)
                .bind(elements[position])
            R.layout.item_addlist -> (holder as AddViewHolder)
                .bind()
        }
    }

    inner class ListViewHolder(
        binding: ItemListBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        private val listCard = binding.listCard
        private val nameview = binding.listNameView

        fun bind(item: ListEntity) {
            nameview.text = item.name
            listCard.setOnClickListener {
                clickHandler(CardAction.ListCardClicked(item))
            }
        }
    }

    inner class AddViewHolder(
        binding: ItemAddlistBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private val addCard = binding.addCard

        fun bind() {
            addCard.setOnClickListener {
                clickHandler(CardAction.AddCardClicked(AddType.IsList))
            }
        }
    }
}
typealias ClickHandler = (CardAction) -> Unit