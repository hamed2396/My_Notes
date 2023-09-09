package com.example.mynotes.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.databinding.ItemNoteBinding
import javax.inject.Inject

class NoteAdapter @Inject constructor() : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private lateinit var binding: ItemNoteBinding
    private var notes = emptyList<NoteEntity>()

    @Inject
    lateinit var entity: NoteEntity
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = notes.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteEntity) {
            binding.apply {
                txtTitle.text = item.title
                txtDescription.text = item.description
                itemLay.setBackgroundColor(ContextCompat.getColor(context, item.colorHex))
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }

                }
                root.setOnLongClickListener {
                    onItemLongClickListener?.let {
                        it(item)
                    }
                    true
                }


            }
        }
    }

    private var onItemClickListener: ((NoteEntity) -> Unit)? = null
    fun onItemClickListener(listener: (NoteEntity) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((NoteEntity) -> Unit)? = null
    fun onItemLongClickListener(listener: (NoteEntity) -> Unit) {
        onItemLongClickListener = listener
    }


    private class SetUpDiffUtils(
        private val oldList: List<NoteEntity>,
        private val newList: List<NoteEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

    }

    fun setData(data: List<NoteEntity>) {
        val diffUtil = SetUpDiffUtils(notes, data)
        val result = DiffUtil.calculateDiff(diffUtil)
        notes = data
        result.dispatchUpdatesTo(this)
    }
}