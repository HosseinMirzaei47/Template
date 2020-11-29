//package com.example.template.connection
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.ConnectivityManager.NetworkCallback
//import android.net.Network
//import android.net.NetworkCapabilities
//import android.net.NetworkRequest
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//
//
//@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//class ConnectionStateMonitor : NetworkCallback() {
//    private val networkRequest: NetworkRequest =
//        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
//
//    fun enable(context: Context) {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            connectivityManager.registerNetworkCallback(networkRequest, this)
//        }
//    }
//
//    fun disable(context: Context) {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            connectivityManager.unregisterNetworkCallback(this)
//        }
//    }
//
//    override fun onAvailable(network: Network) {
//        Log.i("baby", "Connected")
//    }
//
//    override fun onUnavailable() {
//        super.onUnavailable()
//        Log.i("baby", "disconnected")
//    }
//
//}