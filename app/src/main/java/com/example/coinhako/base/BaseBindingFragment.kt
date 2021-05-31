package com.example.coinhako.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Inherits Fragment and supports fast generic-binding
 * @author longtran
 * @since 2021/05/29
 * @param T
 */
abstract class BaseBindingFragment<T : ViewDataBinding> : Fragment() {
    protected lateinit var binding: T

    abstract val inflaterMethod: (LayoutInflater, ViewGroup?, Boolean) -> T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflaterMethod.invoke(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        onBindingReady(binding)
        return binding.root
    }

    /**
     * Invoke binding object after object is created, before returning binding.root.
     *
     * @param binding T
     */
    abstract fun onBindingReady(binding: T)
}