package com.example.template.home.ui.fragment

import android.os.Bundle
import android.util.Log
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
       /* binding.button.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCombineTestFragment())
        }*/

        viewModel.article.observe(viewLifecycleOwner, Observer {
            it.withResult(
                onSuccess = {

                    Log.d("mamad", "my lisr=t: ${it} ")
                },
                onLoading = {},
                onError = { showDialog() }
            )

        })
        viewModel.users.observe(viewLifecycleOwner, usersObserver)
    }


    private val usersObserver = Observer<Result<UserRes>> {

    it.withResult(
        onSuccess = { Log.d("mamad4", "sss") },
        onLoading = { Log.d("mamad4", "lll") },
        onError = { showDialog() }
    )
    }

    private fun showDialog() {
//        val dialog = MaterialAlertDialogBuilder(requireContext())
//        dialog.setTitle("Request Failed")
//            .setNegativeButton("Cancel") { _, _ ->
//            }
//            .setPositiveButton("Retry") { _, _ ->
//                viewModel.users.retry()
//            }f
//            .show()
    }
}