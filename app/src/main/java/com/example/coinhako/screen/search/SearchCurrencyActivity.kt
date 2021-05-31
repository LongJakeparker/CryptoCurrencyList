package com.example.coinhako.screen.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.coinhako.dagger.DaggerAppComponent
import com.example.coinhako.databinding.ActivitySearchCurrencyBinding
import com.example.coinhako.model.CryptoCurrency

/**
 * @author longtran
 * @since 31/05/2021
 */
class SearchCurrencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchCurrencyBinding

    private val mViewModel: SearchCurrencyViewModel by viewModels {
        DaggerAppComponent.create().getSearchCurrencyViewModelFactory()
    }

    companion object {
        private const val EXTRA_LIST_CURRENCY = "EXTRA_LIST_CURRENCY"

        fun startForResult(
            fragment: Fragment,
            listCurrency: ArrayList<CryptoCurrency>,
            requestCode: Int
        ) {
            val intent = Intent(fragment.requireContext(), SearchCurrencyActivity::class.java)
            intent.putExtra(EXTRA_LIST_CURRENCY, listCurrency)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchCurrencyBinding.inflate(layoutInflater).apply {
            viewModel = mViewModel
            lifecycleOwner = this@SearchCurrencyActivity
        }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Init data
        val data = intent.getSerializableExtra(EXTRA_LIST_CURRENCY) as ArrayList<CryptoCurrency>
        if (data.size > 0) {
            mViewModel.setData(data)
        } else {
            binding.tvEmptyResult.visibility = View.VISIBLE
        }

        setupRecyclerView()

        observeEvents()
    }

    /**
     * Setups RecyclerView
     */
    private fun setupRecyclerView() {
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchCurrencyActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mViewModel.currencyAdapter.apply {
                setCurrencyListener(mViewModel.clickLuckyListener)
            }
        }
    }

    /**
     * Defines all observe events from LiveData in ViewModel
     */
    private fun observeEvents() {
        mViewModel.apply {
            listCurrency.observe(this@SearchCurrencyActivity) {
                currencyAdapter.submitList(it)
                if (it.size > 0) {
                    binding.tvEmptyResult.visibility = View.GONE
                } else {
                    binding.tvEmptyResult.visibility = View.VISIBLE
                }
            }

            eventLuckyCoin.observe(this@SearchCurrencyActivity) {
                //Sets result OK whenever there is a change
                setResult(Activity.RESULT_OK)
            }

            eventBack.observe(this@SearchCurrencyActivity) {
                finish()
            }
        }
    }


}