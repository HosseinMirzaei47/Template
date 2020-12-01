package com.example.template.core

import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

const val ID_PROGRESS_BAR = 0
lateinit var progressBar: ProgressBar

@BindingAdapter("reactToResult")
fun <T> View.reactToResult(result: Result<T>?) {
    when (result) {
        is Result.Success -> {
            hideProgressBar()
        }
        is Result.Loading -> {
            this.loadingView()
        }
        is Result.Error -> {
            hideProgressBar()
        }
    }
}

private fun View.loadingView() {
    progressBar = ProgressBar(this.context)
    progressBar.id = ID_PROGRESS_BAR

    val layoutParams =
        ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    layoutParams.endToEnd = this.id
    layoutParams.bottomToBottom = this.id
    layoutParams.topToTop = this.id
    layoutParams.startToStart = this.id
    progressBar.layoutParams = layoutParams

    this as ConstraintLayout
    this.addView(progressBar)
}

fun View.hideProgressBar() {
    this as ConstraintLayout
    this.removeView(progressBar)
}