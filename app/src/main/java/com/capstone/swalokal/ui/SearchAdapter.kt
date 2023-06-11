package com.capstone.swalokal.ui.Search

import com.capstone.swalokal.R
import com.capstone.swalokal.databinding.StoreItemBinding


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.swalokal.api.response.DataItem


class SearchAdapter :
    ListAdapter<DataItem, SearchAdapter.StoreViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreItemBinding.inflate(inflater, parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = getItem(position)

        holder.bind(store)

        // fade in

        // to detail page
//        holder.binding.cardView.setOnClickListener {
//            val intent = Intent(it.context, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.EXTRA_ID, userStory.id)
//            holder.itemView.context.startActivity(intent)
//            Log.d("ADAPTER", "halaman detail ${userStory.name}")
//        }

    }

    class StoreViewHolder(val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: DataItem) {
            binding.tvItemName.text = store.name

//            Glide.with(itemView.context)
//                .load(store.)
//                .error(R.drawable.ic_baseline_broken_image_24)
//                .centerCrop()
//                .into(binding.ivItemPhoto)

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldUser: DataItem, newUser: DataItem): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: DataItem, newUser: DataItem): Boolean {
                    return oldUser == newUser
                }
            }
    }
}