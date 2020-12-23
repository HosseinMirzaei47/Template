package com.example.template.core.bindingadapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.LiveTask

@BindingAdapter(value = ["bind:reactToTask", "bind:type"], requireAll = true)
fun <T> View.reactToTask(liveTask: LiveTask<*>?, type: Type) {
    when (liveTask?.result()) {
        is LiveTaskResult.Success -> {
            val state = SituationFactory().executeState(this, type)
            type.loading().success(state.first, state.second as ViewGroup, liveTask)
        }
        is LiveTaskResult.Loading -> {
            val state = SituationFactory().executeState(this, type)
            type.loading().loading(state.first, state.second as ViewGroup, liveTask)
        }
        is LiveTaskResult.Error -> {
            val state = SituationFactory().executeState(this, type)
            type.loading().error(state.first, state.second as ViewGroup, liveTask)
        }
    }
}

@BindingAdapter("visibleOnLoading")
fun View.visibleOnLoading(liveTask: LiveTask<*>?) {
    this.isVisible = LiveTaskResult.Loading == liveTask?.result()
}

@BindingAdapter("disableOnLoading")
fun Button.disableOnLoading(liveTask: LiveTask<*>?) {
    this.isEnabled = LiveTaskResult.Loading != liveTask?.result()
}