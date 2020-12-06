package com.example.template

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
        observeRequestsStatus()
    }

    private fun observeRequestsStatus() {
        RequestsObserver.getInstance().observe(this) { event ->
            when (event) {
                is Result.Success -> {
                }
                is Result.Error -> {
                    val isNotAuthorized =
                        (event.exception is ServerException && event.exception.meta.statusCode == 401) ||
                                (event.exception is HttpException && event.exception.code() == 401)
                    when {
                        event.exception is NoConnectionException -> {
                            noConnectionDialog()
                        }
                        isNotAuthorized -> {
                            unauthorizedDialog()
                        }
                        else -> {
                            errorDialog()
                        }
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    private fun unauthorizedDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("دسترسی غیر مجاز")
            .setCancelable(false)
            .setNegativeButton("خروج") { _, _ ->
                finish()
            }
            .setPositiveButton("اوکی") { _, _ ->
                RequestsObserver.getInstance().retryAll()
            }
            .show()
    }

    private fun noConnectionDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("خطا در اتصال به اینترنت")
            .setCancelable(false)
            .setNeutralButton("ورود به تنظیمات") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .show()
    }

    private fun errorDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("خطا در برقراری ارتباط با سرور")
            .setNegativeButton("انصراف") { _, _ ->
            }
            .setPositiveButton("تلاش مجدد") { _, _ ->
                RequestsObserver.getInstance().retryAll()
            }
            .show()
    }
}