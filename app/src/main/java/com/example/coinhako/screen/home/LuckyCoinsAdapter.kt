package com.example.coinhako.screen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coinhako.base.ItemDiffCallback
import com.example.coinhako.SharedPreferencesManager
import com.example.coinhako.databinding.ItemLuckyCoinsBinding
import com.example.coinhako.model.CryptoCurrency

/**
 * @author longtran
 * @since 30/05/2021
 */
class LuckyCoinsAdapter : ListAdapter<CryptoCurrency, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    interface RemoveLuckyCoinListener {
        fun onRemove(item: CryptoCurrency, newList: ArrayList<CryptoCurrency>)
    }

    /**
     * listener used to communicate with outside of the Adapter
     */
    private var onClickListener: RemoveLuckyCoinListener? = null

    /**
     * Listener used for all item's event click
     * With defines the listener here can prevent the over-created instances which consumes memory
     */
    private val itemClickListener = View.OnClickListener {
        //Remove the item from current list
        val data = it.tag as ItemTag
        val newList = ArrayList(currentList)
        newList.remove(data.item)

        //Remove item in local list
        data.item.base?.let { it1 -> SharedPreferencesManager.removeLuckyCoins(it1) }

        onClickListener?.onRemove(data.item, newList)
    }

    fun setListener(removeLuckyCoinListener: RemoveLuckyCoinListener) {
        onClickListener = removeLuckyCoinListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LuckyCoinsViewHolder(
            ItemLuckyCoinsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cryptoCurrency = getItem(position)
        (holder as LuckyCoinsViewHolder).itemView.tag = ItemTag(cryptoCurrency, holder)
        holder.bind(cryptoCurrency, position)
    }

    class LuckyCoinsViewHolder(
        private val binding: ItemLuckyCoinsBinding,
        private val itemClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CryptoCurrency, position: Int) {
            binding.apply {
                this.item = item
                clickListener = itemClickListener
                executePendingBindings()
            }
        }
    }
}