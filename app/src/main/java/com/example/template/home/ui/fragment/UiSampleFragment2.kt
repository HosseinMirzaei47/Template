package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.template.core.withResult
import com.example.template.databinding.FragmentUiSample2Binding
import com.example.template.home.ui.listadapter.CityAdapter
import com.example.template.home.ui.viewmodel.UiSampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ui_sample2.*

@AndroidEntryPoint
class UiSampleFragment2 : Fragment() {

    private lateinit var binding: FragmentUiSample2Binding
    private val viewModel: UiSampleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUiSample2Binding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myAdapter = CityAdapter(mutableListOf())

        city_recycler.apply {
            adapter = myAdapter
        }

        viewModel.cities.asLiveData().observe(viewLifecycleOwner) {
            it.result()?.withResult(
                {},
                { list ->
                    myAdapter.list = list
                    myAdapter.notifyDataSetChanged()

                },
                {}
            )
        }

        province_et.editText?.addTextChangedListener {
            viewModel.cityUseCase.setParams(it.toString())
            viewModel.cities.retry()
        }
    }

}