package com.example.template.core.bindingadapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.Result
import com.example.template.core.livatask.LiveTask

@BindingAdapter(value = ["bind:reactToTask", "bind:type"], requireAll = true)
fun <T> View.reactToTask(
    liveTask: LiveTask<*>?,
    progressType: ProgressType,
) {
    when (liveTask?.result()) {
        is Result.Success -> {
            val state = SituationFactory().executeState(this, progressType)
            progressType.loading().success(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Loading -> {
            val state = SituationFactory().executeState(this, progressType)
            progressType.loading().loading(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Error -> {
            val state = SituationFactory().executeState(this, progressType)
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