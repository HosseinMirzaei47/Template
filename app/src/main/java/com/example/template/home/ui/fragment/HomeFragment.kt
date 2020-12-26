package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.template.databinding.FragmentHomeBinding
import com.example.template.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
    }

    private fun setOnClicks() {


        viewModel.users


        binding.gotonext.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUiSampleSelectorFragment())
        }

        binding.submit.setOnClickListener {
            viewModel.combinedTasks.run()
        }

    }

}

/*fun <R> Result<R>.toMyResult(): ResultTest<R> {
    return when (this) {
        is Result.Success -> {
            ResultTest.CustomSuccess(this.data)
        }
        is Result.Error -> {
            ResultTest.CustomError(this.exception)
        }
        is Result.Loading -> {
            ResultTest.CustomLoading
        }
    }
}*/
