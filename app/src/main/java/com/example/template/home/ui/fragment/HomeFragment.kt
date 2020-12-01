package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.template.core.Result
import com.example.template.core.withResult
import com.example.template.databinding.FragmentHomeBinding
import com.example.template.home.data.servicemodels.UserRes
import com.example.template.home.ui.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

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
        viewModel.users.observe(viewLifecycleOwner, usersObserver)
    }

    private val usersObserver = Observer<Result<UserRes>> {
        it.withResult(
            onSuccess = {},
            onLoading = {},
            onError = { showDialog() }
        )
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("Request Failed")
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Retry") { _, _ ->
                viewModel.users.retry()
            }
            .show()
    }
}