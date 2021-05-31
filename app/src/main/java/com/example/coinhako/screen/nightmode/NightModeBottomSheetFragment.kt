package com.example.coinhako.screen.nightmode

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import com.example.coinhako.R
import com.example.coinhako.SharedPreferencesManager
import com.example.coinhako.databinding.FragmentBottomSheetNightModeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Inherits BottomSheetDialogFragment to display a option to turn on/off night mode
 * @author longtran
 * @since 30/05/2021
 */
class NightModeBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetNightModeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetNightModeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            //Init text of current mode
            tvStatus.text = if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                getString(R.string.text_status_night_mode_on)
            } else {
                getString(R.string.text_status_night_mode_off)
            }

            clickListener = View.OnClickListener {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        SharedPreferencesManager.setNightMode(false)
                        tvStatus.text = getString(R.string.text_status_night_mode_off)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        SharedPreferencesManager.setNightMode(true)
                        tvStatus.text = getString(R.string.text_status_night_mode_on)
                    }
                }
            }
        }
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        dialog.setOnShowListener { dialogResult ->
            val bottomSheet =
                (dialogResult as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.bg_bottom_sheet_dialog)
        }
    }
}