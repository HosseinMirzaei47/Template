package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.template.R
import com.example.template.core.withResult
import com.example.template.databinding.FragmentUiSample1Binding
import com.example.template.home.ui.viewmodel.UiSampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ui_sample1.*

@AndroidEntryPoint
class UiSampleFragment1 : Fragment() {

    private lateinit var binding: FragmentUiSample1Binding
    private val viewModel: UiSampleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ui_sample1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cityList = mutableListOf<String>()

        val cityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, cityList
        )

        viewModel.cityList.asLiveData().observe(viewLifecycleOwner, {
            it.result()?.withResult(
                {

                },
                {
                    it.withResult(
                        {},
                        { list ->
                            cityList = list
                        },
                        {}
                    )
                },
                {

                }
            )
            cityAdapter.notifyDataSetChanged()
        })

        city_sp.adapter = cityAdapter

        city_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // send new request with the region
                val region = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}