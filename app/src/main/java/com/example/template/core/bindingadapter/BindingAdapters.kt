package com.example.template.core.bindingadapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.LiveTask

@BindingAdapter(value = ["reactToTask", "type", "layout"], requireAll = false)
fun <T> View.reactToTask(
    liveTask: LiveTask<*>?,
    progressType: ProgressType?,
    @LayoutRes layout: Int?,
) {
    val loadingViewType = liveTask?.loadingViewType
    when (liveTask?.result()) {
        is LiveTaskResult.Success -> {
            val viewParent = getViewParent(this, progressType, layout, loadingViewType)
            if (layout == null) {
                progressType ?: loadingViewType.loading()
                    .success(viewParent.view, viewParent.parent as ViewGroup, liveTask)
            }
        }
        is LiveTaskResult.Loading -> {
            val viewParent = getViewParent(this, progressType, layout, loadingViewType)
            if (layout == null) {
                progressType ?: loadingViewType.loading()
                    .loading(viewParent.view, viewParent.parent as ViewGroup, liveTask)
            }
        }
        is LiveTaskResult.Error -> {
            val viewParent = getViewParent(this, progressType, layout, loadingViewType)
            if (layout == null) {
                progressType ?: loadingViewType.loading()
                    .error(viewParent.view, viewParent.parent as ViewGroup, liveTask)
            }
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