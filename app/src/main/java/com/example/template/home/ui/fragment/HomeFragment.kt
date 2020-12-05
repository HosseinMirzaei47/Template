package com.example.template.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.template.R
import com.example.template.core.Result
import com.example.template.core.util.liveTask
import com.example.template.databinding.FragmentHomeBinding
import com.example.template.home.domain.GetUserUseCase
import com.example.template.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var useCase: GetUserUseCase

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    lateinit var selected: String

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
        onSubmitClick()
        spinnerSetup()
    }

    private fun onSubmitClick() {
        binding.submit.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                when (selected) {
                    "1" -> testForOne()
                    "2" -> testForTwo()
                    "3" -> testForThree()
                    "4" -> testForFour()
                    "5" -> testForFive()
                    "6" -> testForSix()
                }
            }
        }
    }

    private fun spinnerSetup() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sp_item,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selected = spinner.selectedItem.toString()
            }

        }
    }

    private suspend fun testForOne() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

    private suspend fun testForTwo() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

    private suspend fun testForThree() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

    private suspend fun testForFour() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

    private suspend fun testForFive() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

    private suspend fun testForSix() {
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
        liveTask {
            emit(Result.Loading)
            emit(useCase(1))
        }
    }

}