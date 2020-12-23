package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.template.R
import kotlinx.android.synthetic.main.fragment_ui_sample3.*
import kotlinx.android.synthetic.main.fragment_ui_sample_selector.*

class UiSampleSelectorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ui_sample_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner_ui_btn.setOnClickListener {
            findNavController().navigate(UiSampleSelectorFragmentDirections.actionUiSampleSelectorFragmentToUiSampleFragment1())
        }

        text_watcher_ui_btn.setOnClickListener {
            findNavController().navigate(UiSampleSelectorFragmentDirections.actionUiSampleSelectorFragmentToUiSampleFragment2())
        }

        notification_ui_btn.setOnClickListener {
            findNavController().navigate(UiSampleSelectorFragmentDirections.actionUiSampleSelectorFragmentToUiSampleFragment3())
        }
    }
}