package com.example.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.template.core.MyApp.Companion.logger
import com.example.template.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        logger.observe(this) { event ->
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("Request Failed")
                .setNegativeButton("Cancel") { _, _ ->
                }
                .setPositiveButton("Retry") { _, _ ->
                    event.action.invoke()
                }
                .show()
        }
    }
}