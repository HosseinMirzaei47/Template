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

        val loading: MediatorLiveData<Boolean> = combineTestViewModel.mediator.isLoading
        loading.observe(viewLifecycleOwner, {
            if (it) {
                test_progress.visibility = View.VISIBLE
            }else{
                test_progress.visibility = View.INVISIBLE
            }
        })
    }
}