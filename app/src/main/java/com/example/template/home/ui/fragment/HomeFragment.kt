package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.template.core.Result.*
import com.example.template.databinding.FragmentHomeBinding
import com.example.template.home.domain.GetUserUseCase
import com.example.template.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var useCase: GetUserUseCase
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()

        observeRequestsStatus()
    }

    private fun setOnClicks() {
        binding.gotonext.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentSelf())
        }

        binding.submit.setOnClickListener {
            viewModel.combinedTasks.execute()
        }
    }

    private fun observeRequestsStatus() {
        viewModel.combinedTasks.asLiveData().observe(viewLifecycleOwner) { event ->
            when (event) {
                is Success -> {
                }
                is Error -> {

                }
                is Loading -> {
                }
            }
        }
    }


}