package com.example.template

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.template.core.MyApp.Companion.loggerTasks
import com.example.template.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        loggerTasks.observe(this) {
            Toast.makeText(
                this,
                "Something went wrong! \n message: ${it.status}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}