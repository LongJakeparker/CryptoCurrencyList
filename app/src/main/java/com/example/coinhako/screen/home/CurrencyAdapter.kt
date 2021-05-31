package com.example.coinhako.screen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coinhako.base.ItemDiffCallback
import com.example.coinhako.SharedPreferencesManager
import com.example.coinhako.model.CryptoCurrency
import com.example.coinhako.databinding.ItemCryptoCurrencyBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author longtran
 * @since 29/05/2021
 */
class CurrencyAdapter: ListAdapter<CryptoCurrency, RecyclerView.ViewHolder>(ItemDiffCallback()) {
    private val compositeDisposable = CompositeDisposable()

    interface CurrencyListener {
        fun onClickLucky(item: CryptoCurrency)
    }

    /**
     * listener used to communicate with outside of the Adapter
     */
    private var onClickListener: CurrencyListener? = null

    /**
     * Listener used for all item's event click
     * With defines the listener here can prevent the over-created instances which consumes memory
     */
    private val itemClickListener = View.OnClickListener {
        //Change the isLucky flag to marks/unMarks the currency as a favorite
        //Only change the UI of this item, only save after user stop clicked item for 0.4s
        val data = it.tag as ItemTag
        data.item.isLucky = !data.item.isLucky
        notifyItemChanged(data.viewHolder.adapterPosition)

        //Get the disposable and add it to CompositeDisposable
        val disposable = (data.viewHolder as CryptoCurrencyViewHolder).getClickItemDisposable(data.item, onClickListener)
        compositeDisposable.remove(disposable)
        compositeDisposable.add(disposable)
    }

    fun setCurrencyListener(currencyListener: CurrencyListener) {
        onClickListener = currencyListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CryptoCurrencyViewHolder(
            ItemCryptoCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cryptoCurrency = getItem(position)
        (holder as CryptoCurrencyViewHolder).itemView.tag = ItemTag(cryptoCurrency, holder)
        holder.bind(cryptoCurrency, position)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        //Disposes all Disposable when adapter is detached from recyclerView to prevent leaks
        compositeDisposable.dispose()
    }

    class CryptoCurrencyViewHolder(
        private val binding: ItemCryptoCurrencyBinding,
        private val itemClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private val INTERVAL_UPDATE_SLOTS_BLOCK_IN_MILLI_SEC = 400L
        private var disposable : Disposable? = null

        fun bind(item: CryptoCurrency, position: Int) {
            binding.apply {
                this.item = item
                clickListener = itemClickListener
                executePendingBindings()
            }
        }

        /**
         * Disposes the current Disposable and create the new one which have timer is 0.4s
         * to prevents fast-clicking
         * Means only do action when user stopped interact at least 0.4s
         */
        fun getClickItemDisposable(item: CryptoCurrency, onClickListener: CurrencyListener?): Disposable {
            //Keep reset the timer 0.4 second when user click
            disposable?.dispose()

            disposable = Observable.timer(INTERVAL_UPDATE_SLOTS_BLOCK_IN_MILLI_SEC, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    item.base?.let { base ->
                        //Only do action when the flag isLucky is really changed
                        //ex: Fast clicking the item twice means there is nothing changed
                        if ((item.isLucky && SharedPreferencesManager.isContainCoin(item.base))
                            || (!item.isLucky && !SharedPreferencesManager.isContainCoin(item.base))) {
                            return@doOnComplete
                        }

                        SharedPreferencesManager.putLuckyCoins(base)

                        onClickListener?.onClickLucky(item)
                    }
                }
                .subscribe()

            return disposable!!
        }
    }
}

/**
 * Custom tag which contain data can use outside the ViewHolder
 */
data class ItemTag(val item: CryptoCurrency, val viewHolder: RecyclerView.ViewHolder)