package com.bassem.myapplication.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bassem.myapplication.model.AsteroidModel
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/2/2023.
 */
class MainAdapter(private val onClickListener: OnClickListener) : ListAdapter<AsteroidModel, MainAdapter.AsteroidViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val withDataBinding: ItemAsteroidBinding = ItemAsteroidBinding.inflate(LayoutInflater.from(parent.context))
        return AsteroidViewHolder(withDataBinding)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    class AsteroidViewHolder(private var binding: ItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroidModel: AsteroidModel) {
            binding.asteroidItem = asteroidModel
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<AsteroidModel>() {
        override fun areItemsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (asteroidModel: AsteroidModel) -> Unit) {
        fun onClick(asteroidModel: AsteroidModel) = clickListener(asteroidModel)
    }
}