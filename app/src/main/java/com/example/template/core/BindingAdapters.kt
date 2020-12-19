package com.example.template.core
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.util.LiveTask

@BindingAdapter("reactToTask")
fun <T> View.reactToTask(liveTask: LiveTask<*>?) {
    when (liveTask?.result()) {
        is Result.Success -> {
            val state = SituationFactory().executeState(this)
            SuccessState().handleState(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Loading -> {
            val state = SituationFactory().executeState(this)
            LoadingState().handleState(state.first, state.second as ViewGroup, liveTask)
        }
        is Result.Error -> {
            val state = SituationFactory().executeState(this)
            ErrorState().handleState(state.first, state.second as ViewGroup, liveTask)
        }
    }
}

enum class Type {
    INDICATOR, LINEAR
}

@BindingAdapter("type")
fun <T> View.type(type: Type) {

}

@BindingAdapter("visibleOnLoading")
fun View.visibleOnLoading(liveTask: LiveTask<*>?) {
    this.isVisible = Result.Loading == liveTask?.result()
}

@BindingAdapter("disableOnLoading")
fun Button.disableOnLoading(liveTask: LiveTask<*>?) {
    this.isEnabled = Result.Loading != liveTask?.result()
}