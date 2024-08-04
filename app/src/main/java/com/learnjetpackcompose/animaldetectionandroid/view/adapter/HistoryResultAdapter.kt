package com.learnjetpackcompose.animaldetectionandroid.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learnjetpackcompose.animaldetectionandroid.data.entity.ResultEntity
import com.learnjetpackcompose.animaldetectionandroid.databinding.ItemResultHistoryBinding

class HistoryResultAdapter(
    private val onDeleteClick: (ResultEntity) -> Unit
) : ListAdapter<ResultEntity, HistoryResultAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemResultHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val resultHistory = getItem(position)
        holder.bind(resultHistory)
    }

    class MyViewHolder(
        private val binding: ItemResultHistoryBinding,
        private val onDeleteClick: (ResultEntity) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(resultEntity: ResultEntity) {
            binding.tvResultHistory.text = resultEntity.result
            binding.tvResultDateHistory.text = resultEntity.timeStamps
            Glide.with(itemView.context)
                .load(resultEntity.imageUri)
                .into(binding.ivResultHistory)

            binding.buttonDeleteResultHistory.setOnClickListener {
                onDeleteClick(resultEntity)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ResultEntity> =
            object : DiffUtil.ItemCallback<ResultEntity>() {
                override fun areItemsTheSame(
                    oldItem: ResultEntity,
                    newItem: ResultEntity
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ResultEntity,
                    newItem: ResultEntity
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}