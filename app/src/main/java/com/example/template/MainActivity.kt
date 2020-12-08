package com.example.template

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.template.core.Logger
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.NoInternetException
import com.example.template.core.util.ServerException
import com.example.template.core.util.UnAuthorizedException
import com.example.template.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        Logger.observe(this) {
            val isNotAuthorized =
                (it.exception is ServerException && it.exception.meta.statusCode == 401) ||
                        (it.exception is HttpException && it.exception.code() == 401)
            if (it.exception is UnAuthorizedException || isNotAuthorized) {
                unauthorizedDialog()
            } else if (it.exception is NoConnectionException) {
                noConnectionDialog()
            } else if (it.exception is NoInternetException) {
                errorDialog()
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
            }
            .show()
    }
}