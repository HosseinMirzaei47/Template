package com.example.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.template.core.Result
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.RequestsObserver
import com.example.template.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        RequestsObserver.observe(this) { event ->
            when (event) {
                is Result.Success -> {
                }
                is Result.Error -> {
                    if (event.exception is NoConnectionException) {
                        showDialog()
                    } else {
                        showDialog("ارتباط به درستی بر قرار نشد")
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    private fun showDialog(s: String) {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle(s)
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Retry") { _, _ ->
                RequestsObserver.retry()
            }
            .show()
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("کلا اتصال بر قرار نیست")
            .setNeutralButton("خایلوخو") { _, _ ->
            }
            .show()
    }
}