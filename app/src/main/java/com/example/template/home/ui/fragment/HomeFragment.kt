package com.example.template.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.template.core.Result
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.ServerException
import com.example.template.databinding.FragmentHomeBinding
import com.example.template.home.domain.GetUserUseCase
import com.example.template.home.ui.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
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
            viewModel.combinedTasks.executeAll()
        }
    }

    private fun observeRequestsStatus() {
        viewModel.combinedTasks.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Result.Success<*> -> {
                }
                is Result.Error -> {
                    val isNotAuthorized =
                        (event.exception is ServerException && event.exception.meta.statusCode == 401) ||
                                (event.exception is HttpException && event.exception.code() == 401)
                    when {
                        event.exception is NoConnectionException -> {
                            noConnectionDialog()
                        }
                        isNotAuthorized -> {
                            unauthorizedDialog()
                        }
                        else -> {
                            errorDialog()
                        }
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    private fun unauthorizedDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("دسترسی غیر مجاز")
            .setCancelable(false)
            .setNegativeButton("خروج") { _, _ ->
                requireActivity().finish()
            }
            .setPositiveButton("تلاش برای لاگین موفق") { _, _ ->
                viewModel.combinedTasks.retryAll()
            }
            .show()
    }

    private fun noConnectionDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("خطا در اتصال به اینترنت")
            .setCancelable(false)
            .setNeutralButton("ورود به تنظیمات") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .show()
    }

    private fun errorDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("خطا در برقراری ارتباط با سرور")
            .setNegativeButton("انصراف") { _, _ ->
            }
            .setPositiveButton("تلاش مجدد") { _, _ ->
            }
            .show()
    }
}