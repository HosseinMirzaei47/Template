package com.example.template.core.bindingadapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.Result
import com.example.template.core.livatask.LiveTask

@BindingAdapter(value = ["bind:reactToTask", "bind:type", "bind:layout"], requireAll = true)
fun <T> View.reactToTask(
    liveTask: LiveTask<*>?,
    progressType: ProgressType,
    @LayoutRes layout: Int,
) {
    when (liveTask?.result()) {
        is Result.Success -> {
            val state = SituationFactory().executeState(this, progressType, layout)
            progressType.loading().success(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Loading -> {
            val state = SituationFactory().executeState(this, progressType, layout)
            progressType.loading().loading(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Error -> {
            val state = SituationFactory().executeState(this, progressType, layout)
            progressType.loading().error(state.first, state.second as ViewGroup, liveTask)
        }
    }
}

@BindingAdapter("visibleOnLoading")
fun View.visibleOnLoading(liveTask: LiveTask<*>?) {
    this.isVisible = Result.Loading == liveTask?.result()
}

@BindingAdapter("disableOnLoading")
fun Button.disableOnLoading(liveTask: LiveTask<*>?) {
    this.isEnabled = Result.Loading != liveTask?.result()
}