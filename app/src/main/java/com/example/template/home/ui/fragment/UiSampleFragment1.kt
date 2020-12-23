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
import com.example.template.core.withResult
import com.example.template.databinding.FragmentUiSample1Binding
import com.example.template.home.ui.viewmodel.UiSampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ui_sample1.*

@AndroidEntryPoint
class UiSampleFragment1 : Fragment() {

    private lateinit var binding: FragmentUiSample1Binding
    private val viewModel: UiSampleViewModel by viewModels()
    var isCityAssigned = false
    var isRegionAssigned = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUiSample1Binding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityList = mutableListOf<String>()
        val regionList = mutableListOf<String>()

        val cityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, cityList
        )

        val regionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, regionList
        )

        viewModel.cityList.asLiveData().observe(viewLifecycleOwner) {
            it.result()?.let { result ->
                result.withResult(
                    {},
                    { list ->
                        cityAdapter.addAll(list)
                    },
                    {

                    }
                )
            }
        }

        viewModel.regionList.asLiveData().observe(viewLifecycleOwner, {
            it.result()?.withResult(
                {},
                { list ->
                    regionAdapter.clear()
                    regionAdapter.addAll(list)
                },
                { exception ->
                    regionAdapter.clear()
                    regionAdapter.addAll(exception.message)
                }
            )
        })
        viewModel.regionCode.asLiveData().observe(viewLifecycleOwner, {
            it.result()?.withResult(
                {},
                { list ->
                    code_tv.text = list[0]
                },
                {
                }
            )
        })

        city_sp.adapter = cityAdapter
        region_sp.adapter = regionAdapter

        city_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                isCityAssigned = parent.selectedItem.toString() != "N/A"
                viewModel.regionUseCase.setParams(parent.selectedItem.toString())
                viewModel.regionList.retry()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        region_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                isRegionAssigned = parent.selectedItem.toString() != "N/A"
                viewModel.regionCodeUseCase.setParams(parent.selectedItem.toString())
                viewModel.regionCode.retry()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.combined.asLiveData().observe(viewLifecycleOwner, {
            it.result()?.withResult(
                { isLoading ->
                    if (isLoading) {
                        submit_btn.isEnabled = false
                        submit_btn.text = "N/A"
                    }
                },
                {
                    if (isCityAssigned && isRegionAssigned) {
                        submit_btn.isEnabled = true
                        submit_btn.text = "Submit"
                    }
                },
                {

                }
            )
        })

        viewModel.cityList.run()
    }
}