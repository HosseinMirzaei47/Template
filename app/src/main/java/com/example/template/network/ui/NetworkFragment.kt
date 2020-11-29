package com.example.template.network.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.template.R
import com.example.template.connection.ConnectionLiveData
import com.example.template.connection.ConnectivityChangeReceiver

class NetworkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(viewLifecycleOwner, { isConnected ->
            if (isConnected != null) {
                if (isConnected) {
                    Toast.makeText(requireContext(), "network connected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "network disconnected", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

}