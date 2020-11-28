package com.example.template.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectivityChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("*** Action: " + intent.action)
//        if (intent.action.equals("android.net.conn.CONNECTIVITY_CHANGE", ignoreCase = true)) {
//            Toast.makeText(context, "Connection changed", Toast.LENGTH_SHORT).show()
//        }
    }

}