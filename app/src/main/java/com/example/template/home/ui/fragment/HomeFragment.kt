package com.example.template.home.ui.fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.template.R
import com.example.template.core.util.Resource
import com.example.template.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel:HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userResResponse.observe(viewLifecycleOwner, { result->
            Log.d("mamad", "${result}")
            when(result){
                is Resource.Success->{
                    Log.d("mamad", "${result.data}")
                }
                is Resource.Loading->{}
                is Resource.Error->{}

            }
        })

    }
}