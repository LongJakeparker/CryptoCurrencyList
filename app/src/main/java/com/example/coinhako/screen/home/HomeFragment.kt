package com.example.coinhako.screen.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinhako.base.BaseBindingFragment
import com.example.coinhako.base.Status
import com.example.coinhako.custom.AppBarStateChangeListener
import com.example.coinhako.custom.DisableAppBarLayoutBehavior
import com.example.coinhako.dagger.DaggerAppComponent
import com.example.coinhako.databinding.FragmentHomeBinding
import com.example.coinhako.screen.nightmode.NightModeBottomSheetFragment
import com.example.coinhako.screen.search.SearchCurrencyActivity
import com.google.android.material.appbar.AppBarLayout


/**
 * @author longtran
 * @since 29/05/2021
 */
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {
    private val REQUEST_CODE_SEARCH = 101
    private var isFirstInit = false //Flag detects whenever the Fragment init view the first time

    //Defines a AppBarLayout custom behavior with the drag behavior is disable
    private val customBehavior = DisableAppBarLayoutBehavior().apply {
        setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }
    private var appBarState = AppBarStateChangeListener.State.COLLAPSED

    private val mViewModel: HomeViewModel by activityViewModels {
        DaggerAppComponent.create().getHomeViewModelFactory()
    }

    /**
     * Listener to notifies whenever AppBar's State is changed
     */
    private val appBarOffsetChangeListener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
            //Save the State to use in HomeFragment
            if (state != null) {
                appBarState = state
            }

            //Enables/Disables the swipeRefreshLayout based on the AppBar's State
            //Only enable when the entire view is completely at the top
            //If don't do this there will be a conflict between the scroll-up and pull refresh behavior
            when (state) {
                State.EXPANDED -> {
                    binding.swipeRefreshLayout.isEnabled = true
                }
                State.COLLAPSED -> {
                    //State collapsed only enable the refresh layout when
                    //the lucky coin list is empty and currency list is completely at the top
                    //otherwise, the entire view is not completely at the top
                    binding.swipeRefreshLayout.isEnabled = (mViewModel.listLuckyCoins.value!!.isEmpty() && isFirstItemDisplaying())
                }
                else -> {
                    binding.swipeRefreshLayout.isEnabled = false
                }
            }

            //Show the text base on the the State
            //Collapsed shows: @string/text_title_collect_lucky_coins
            //Expanded shows: @string/text_title_lucky_coins
            mViewModel.apply {
                if (state == State.COLLAPSED) {
                    showLuckyCoinsText(false)
                } else if (state == State.EXPANDED) {
                    showLuckyCoinsText(true)
                }
            }
        }
    }

    override val inflaterMethod: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun onBindingReady(binding: FragmentHomeBinding) {
        setupRecyclerViews()

        binding.apply {
            viewModel = mViewModel

            swipeRefreshLayout.setOnRefreshListener {
                getData()
                enableScroll(false)
            }

            appBarLayout.addOnOffsetChangedListener(appBarOffsetChangeListener)
            setCollapsingBehavior(false)
        }

        //Only fetch new data when list currency is empty
        if (mViewModel.listCurrency.value!!.isEmpty()) {
            isFirstInit = true
            getData()
        }

        observeEvents()
    }

    /**
     * Setups RecyclerViews
     */
    private fun setupRecyclerViews() {
        binding.rvCurrencies.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = mViewModel.currencyAdapter.apply {
                setCurrencyListener(mViewModel.clickLuckyListener)
            }

            //Add a listener to enable when change lucky list items while scrolling the currency list
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.swipeRefreshLayout.isEnabled = (mViewModel.listLuckyCoins.value!!.isEmpty() && isFirstItemDisplaying())
                }
            })
        }

        binding.rvLuckyCoins.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = mViewModel.luckyCoinsAdapter.apply {
                setListener(mViewModel.removeLuckyCoinListener)
            }
        }
    }

    /**
     * Defines all observe events from LiveData in ViewModel
     */
    private fun observeEvents() {
        mViewModel.apply {
            listCurrency.observe(viewLifecycleOwner) {
                currencyAdapter.submitList(it)
            }

            listLuckyCoins.observe(viewLifecycleOwner) {
                val isAddNewItem = it.size > luckyCoinsAdapter.itemCount
               luckyCoinsAdapter.submitList(it)

                binding.apply {
                    if (isAddNewItem) {
                        rvLuckyCoins.smoothScrollToPosition(0)
                    }

                    //Expands/Collapses the AppBar based on the data is empty or not
                    //If user is scrolling the currency list or the list is not at the top
                    //then keep the current state an skip changing it only change the behavior
                    if (isFirstItemDisplaying()) {
                        if (it.isNotEmpty() && (appBarState == AppBarStateChangeListener.State.COLLAPSED || isFirstInit)) {
                            setExpandableAndBehavior(true)
                            isFirstInit = false
                        } else if (it.isEmpty()) {
                            setExpandableAndBehavior(false)
                        }
                    } else {
                        setCollapsingBehavior(it.isNotEmpty())
                    }
                }
            }

            eventSearch.observe(viewLifecycleOwner) {
                SearchCurrencyActivity.startForResult(this@HomeFragment, mViewModel.listCurrency.value!!, REQUEST_CODE_SEARCH)
            }

            eventSettingNightMode.observe(viewLifecycleOwner) {
                val nightModeBottomSheetFragment = NightModeBottomSheetFragment()
                nightModeBottomSheetFragment.show(childFragmentManager, null)
            }

            eventReTry.observe(viewLifecycleOwner) {
                getData()
            }
        }
    }

    /**
     * Sets AppBar State and behavior
     * @param expanded
     * @param animate
     */
    private fun setExpandableAndBehavior(expanded: Boolean, animate: Boolean = true) {
        binding.appBarLayout.setExpanded(expanded, animate)
        setCollapsingBehavior(expanded)
    }

    /**
     * Sets AppBar behavior to enable/disable scrolling
     * @param isEnable
     */
    private fun setCollapsingBehavior(isEnable: Boolean) {
        val layoutParams = binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        customBehavior.setEnabled(isEnable)
        layoutParams.behavior = customBehavior
    }

    /**
     * Gets data from server
     */
    private fun getData() {
        mViewModel.apply {
            getDataListCurrency().observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        if (mViewModel.listCurrency.value!!.isEmpty()) {
                            binding.apply {
                                loadingView.visibility = View.VISIBLE
                                swipeRefreshLayout.isEnabled = false
                                llError.visibility = View.GONE
                            }
                        }
                    }

                    Status.SUCCESS -> {
                        onFetchDateFinish()
                        binding.llError.visibility = View.GONE
                        mViewModel.setDataListCurrency(ArrayList(it.data))
                    }

                    Status.ERROR -> {
                        onFetchDateFinish()
                        //If current data is empty then show error view
                        //else show a toast which displays error message
                        if (mViewModel.listCurrency.value!!.isEmpty()) {
                            binding.llError.visibility = View.VISIBLE
                        } else {
                            it.exception?.let { ex ->
                                Toast.makeText(requireContext(), ex.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onFetchDateFinish() {
        enableScroll(true)
        binding.swipeRefreshLayout.isEnabled = true
        binding.loadingView.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Check if the currency list is completely at the top
     */
    private fun isFirstItemDisplaying(): Boolean {
        val layoutManager = binding.rvCurrencies.layoutManager as LinearLayoutManager
        val pos = layoutManager.findFirstVisibleItemPosition()
        return (layoutManager.findViewByPosition(pos)?.top == 0 && pos == 0) || pos < 0
    }

    /**
     * Displays a view to prevent scroll the currency list while data is refreshing
     */
    private fun enableScroll(enableScroll: Boolean) {
        if (enableScroll) {
            binding.disableDragView.visibility = View.GONE
        } else {
            binding.disableDragView.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //If back from search and the is a change then refresh the data
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == Activity.RESULT_OK) {
            getData()
        }
    }
}