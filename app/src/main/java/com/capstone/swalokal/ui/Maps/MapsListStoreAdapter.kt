package com.capstone.swalokal.ui.Search

import com.capstone.swalokal.R
import com.capstone.swalokal.databinding.StoreItemBinding


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.swalokal.api.response.PredictItem
import com.capstone.swalokal.ui.DetailStore.DetailStoreActivity


class MapsListStoreAdapter :
    ListAdapter<PredictItem, MapsListStoreAdapter.StoreViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreItemBinding.inflate(inflater, parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = getItem(position)

        if (store != null && store.toko?.isNotEmpty() != false) {
            holder.bind(store)
        }

        // to detail page
        holder.binding.cardView.setOnClickListener {
            Log.d("MapsAdapter", "Detail ${store.toko} di klik")
            val intent = Intent(it.context, DetailStoreActivity::class.java)
            intent.putExtra("dataItem", store)
            it.context.startActivity(intent)
        }

    }

    class StoreViewHolder(val binding: StoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: PredictItem) {
            binding.tvItemName.text = store.toko

            Glide.with(itemView.context)
                .load(store.photoUrl)
                .error(R.drawable.ic_gallery_27)
                .centerCrop()
                .into(binding.ivItemPhoto)

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PredictItem> =
            object : DiffUtil.ItemCallback<PredictItem>() {
                override fun areItemsTheSame(oldUser: PredictItem, newUser: PredictItem): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: PredictItem,
                    newUser: PredictItem,
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }
}