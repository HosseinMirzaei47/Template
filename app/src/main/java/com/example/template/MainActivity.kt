package com.example.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.template.core.Result
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.RequestsObserver
import com.example.template.core.util.ServerException
import com.example.template.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

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
                    } else if (event.exception is ServerException || (event.exception is HttpException && event.exception.code() == 401)) {
                        showUnauthorizedDialog()
                    } else {
                        showDialog("ارتباط به درستی بر قرار نشد")
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    private fun showUnauthorizedDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("دسترسی شما غیر مجاز است مجددا لاگین کنید")
            .setCancelable(false)
            .setNegativeButton("لاگین نا موفق") { _, _ ->
                finish()
            }
            .setPositiveButton("لاگین موفق") { _, _ ->
                RequestsObserver.retry()
            }
            .show()
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