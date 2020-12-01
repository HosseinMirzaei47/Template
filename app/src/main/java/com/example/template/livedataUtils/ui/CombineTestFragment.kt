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
import com.example.template.livedataUtils.data.Resource
import com.example.template.livedataUtils.data.Status
import kotlinx.android.synthetic.main.fragment_comibine_test.*

class CombineTestFragment : Fragment() {

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
            if (it.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        combineTestViewModel.live2.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        val loading: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner.isLoading
        loading.observe(viewLifecycleOwner, {
            if (it.status == Status.LOADING) {
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
            if (it.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        combineTestViewModel.live4.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
            }
        })

        val loading2: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner2.isLoading
        loading2.observe(viewLifecycleOwner, {
            if (it.status == Status.LOADING) {
                test_progress_2.visibility = View.VISIBLE
            } else {
                test_progress_2.visibility = View.INVISIBLE
            }
        })

        // ************************************ Total ***********************************************

        val loading3: MediatorLiveData<Resource<Any>> = combineTestViewModel.combiner3.isLoading
        loading3.observe(viewLifecycleOwner, {
            if (it.status == Status.LOADING) {
                test_progress_3.visibility = View.VISIBLE
                layout_fade.visibility = View.VISIBLE
            } else {
                test_progress_3.visibility = View.INVISIBLE
                layout_fade.visibility = View.INVISIBLE
            }
        })

    }
}