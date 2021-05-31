package com.example.coinhako.base

import androidx.recyclerview.widget.DiffUtil

/**
 * DiffUtil for base ListAdapter
 * @author longtran
 * @since 2021/05/29
 */
class ItemDiffCallback<T : IComparable<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}

interface IComparable<T> {
    fun isTheSame(item: T): Boolean
    fun areContentsTheSame(item: T): Boolean
}