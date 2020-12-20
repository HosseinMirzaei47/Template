package com.example.template.core

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.util.LiveTask

enum class Type {
    INDICATOR, LINEAR, SANDY_CLOCK
}

@BindingAdapter(value = ["bind:reactToTask", "bind:type"], requireAll = true)
fun <T> View.reactToTask(liveTask: LiveTask<*>?, type: Type) {
    when (liveTask?.result()) {
        is Result.Success -> {
            val state = SituationFactory().executeState(this, type)
            SuccessState().handleState(state.first, state.second as ViewGroup, liveTask, type)
        }
        is Result.Loading -> {
            val state = SituationFactory().executeState(this, type)
            LoadingState().handleState(state.first, state.second as ViewGroup, liveTask, type)
        }
        is Result.Error -> {
            val state = SituationFactory().executeState(this, type)
            ErrorState().handleState(state.first, state.second as ViewGroup, liveTask, type)
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