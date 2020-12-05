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
        observeRequestsStatus()
    }

    private fun observeRequestsStatus() {
        RequestsObserver.getInstance().observe(this) { event ->
            when (event) {
                is Result.Success -> {
                }
                is Result.Error -> {
                    when {
                        event.exception is NoConnectionException -> {
                            noConnectionDialog()
                        }
                        (event.exception is ServerException &&
                                event.exception.meta.statusCode == 401) ||
                                (event.exception is HttpException &&
                                        event.exception.code() == 401) -> {
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
        dialog.setTitle("دسترسی شما غیر مجاز است مجددا لاگین کنید")
            .setCancelable(false)
            .setNegativeButton("لاگین نا موفق") { _, _ ->
                finish()
            }
            .setPositiveButton("لاگین موفق") { _, _ ->
                RequestsObserver.getInstance().retryAll()
            }
            .show()
    }

    private fun noConnectionDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("هیچ اتصالی برای اینترنت موجود نمی باشد")
            .setCancelable(false)
            .setNeutralButton("ورود به تنظیمات") { _, _ ->
            }
            .show()
    }

    private fun errorDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle(" برقراری ارتباط با سرور مقدور نمی باشد")
            .setNegativeButton("انصراف") { _, _ ->
            }
            .setPositiveButton("تلاش مجدد") { _, _ ->
                RequestsObserver.getInstance().retryAll()
            }
            .show()
    }
}