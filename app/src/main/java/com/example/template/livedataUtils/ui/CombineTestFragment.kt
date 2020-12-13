package com.example.template.livedataUtils.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.template.R
import com.example.template.core.util.Resource
import kotlinx.android.synthetic.main.fragment_comibine_test.*

class CombineTestFragment : Fragment() {

    var isError = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comibine_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val combineTestViewModel = ViewModelProvider(this).get(CombineTestViewModel::class.java)

        // ************************************ half 1 ***********************************************

        test_btn_1.setOnClickListener {
            combineTestViewModel.liveChange1(et_test_1.text.toString())
        }

        test_btn_2.setOnClickListener {
            combineTestViewModel.liveChange2(et_test_2.text.toString())
        }

        combineTestViewModel.live1.observe(viewLifecycleOwner, {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            } else if (it is Resource.Error) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        combineTestViewModel.live2.observe(viewLifecycleOwner, {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            } else if (it is Resource.Error) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        val loading: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner.result
        loading.observe(viewLifecycleOwner, {
            if (it is Resource.Loading) {
                test_progress.visibility = View.VISIBLE
            } else {
                test_progress.visibility = View.INVISIBLE
            }
        })

        // ************************************ half 2 ***********************************************

        test_btn_3.setOnClickListener {
            combineTestViewModel.liveChange3(et_test_3.text.toString())
        }

        test_btn_4.setOnClickListener {
            combineTestViewModel.liveChange4(et_test_4.text.toString())
        }

        combineTestViewModel.live3.observe(viewLifecycleOwner, {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            } else if (it is Resource.Error) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        combineTestViewModel.live4.observe(viewLifecycleOwner, {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            } else if (it is Resource.Error) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        val loading2: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner2.result
        loading2.observe(viewLifecycleOwner, {
            if (it is Resource.Loading) {
                test_progress_2.visibility = View.VISIBLE
            } else {
                test_progress_2.visibility = View.INVISIBLE
            }
        })

        // ************************************ Total ***********************************************

        val loading3: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner3.result
        loading3.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    if (!isError) {
                        test_progress_3.visibility = View.VISIBLE
                        layout_fade.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    isError = true
                    test_progress_3.visibility = View.INVISIBLE
                    tv_error.visibility = View.VISIBLE
                    layout_fade.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    isError = false
                    test_progress_3.visibility = View.INVISIBLE
                    tv_error.visibility = View.INVISIBLE
                    layout_fade.visibility = View.INVISIBLE
                }
            }
        })

    }
}