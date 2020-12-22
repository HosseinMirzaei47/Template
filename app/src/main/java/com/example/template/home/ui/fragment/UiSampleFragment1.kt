package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.template.R
import com.example.template.core.Result
import com.example.template.core.withResult
import com.example.template.home.ui.viewmodel.UiSampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ui_sample1.*

@AndroidEntryPoint
class UiSampleFragment1 : Fragment() {

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
        var regionList = mutableListOf<String>()

        val cityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, cityList
        )

        val regionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, regionList
        )

        viewModel.cityList.asLiveData().observe(viewLifecycleOwner, { it ->
            println("mmb $it")
            println("mmb ${it.result()}")
            when (val result =it.result()) {
                is Result.Success -> {
                    println("mmb ${result.data}")
                }
                is Result.Error -> {

                }
                Result.Loading -> {
                }
            }
//            it.result()?.let { result ->
//                result.withResult(
//                    { isLoading ->
//                        if (isLoading) {
//                            Toast.makeText(requireContext(), "city loading", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    },
//                    {
////                    it.withResult(
////                        {},
////                        { list ->
////                            //cityList = list
////                            cityAdapter.notifyDataSetChanged()
////                        },
////                        {}
////                    )
//                    },
//                    {
//
//                    }
//                )
//            }
        })

        viewModel.regionList.asLiveData().observe(viewLifecycleOwner, {
            it.result()?.withResult(
                { isLoading ->
                    if (isLoading) {
                        Toast.makeText(requireContext(), "regions loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                {
                    it.withResult(
                        {},
                        { list ->
                            regionList = list
                            regionAdapter.notifyDataSetChanged()
                        },
                        {}
                    )
                },
                {

                }
            )
        })
        // loading

        city_sp.adapter = cityAdapter
        region_sp.adapter = regionAdapter

        city_sp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                viewModel.regionUseCase.setParams(parent.selectedItem.toString())
                viewModel.regionList.retry()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.cityList.run()
    }
}