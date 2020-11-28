package com.example.template.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class ConnectivityChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.EXTRA_NETWORK == intent.action) {
            val noConnectivity = intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            )
            if (noConnectivity) {
                Toast.makeText(context, "disconnected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

}