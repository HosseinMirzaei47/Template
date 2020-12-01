package com.example.template.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.template.R
import kotlinx.android.synthetic.main.layout_state.view.*

private const val LOAD_STATE = "loading"
private const val ERROR_STATE = "error"
private const val SUCCESS_STATE = "success"
var hashMap: HashMap<Int, View> = HashMap()

@BindingAdapter("reactToResult")
fun <T> View.reactToResult(result: Result<T>?) {
    this as ConstraintLayout
    val stateLayout = getStateLayout(this.context, this)
    when (result) {
        is Result.Success -> {
            showLoadingState(SUCCESS_STATE, stateLayout)
        }
        is Result.Loading -> {
            showLoadingState(LOAD_STATE, stateLayout)
        }
        is Result.Error -> {
            showLoadingState(ERROR_STATE, stateLayout)
        }
    }
}

fun getStateLayout(context: Context, viewGroup: ViewGroup): View? {
    if (!hashMap.containsKey(viewGroup.id)) {
        val stateLayout =
            LayoutInflater.from(context).inflate(R.layout.layout_state, viewGroup, false)
        viewGroup.addView(stateLayout)
        hashMap[viewGroup.id] = stateLayout
    }
    return hashMap[viewGroup.id]
}

fun View.showLoadingState(state: String, stateLayout: View?) {
    this as ConstraintLayout
    when (state) {
        LOAD_STATE -> {
            stateLayout?.let {
                it.layoutParams.height = 60
                it.ivBtn_refresh.visibility = View.INVISIBLE
                it.tv_status.text = LOAD_STATE
                it.ivBtn_close.setOnClickListener { _ ->
                    this.removeView(it)
                }
            }
        }
        ERROR_STATE -> {
            stateLayout?.let {
                it.ivBtn_refresh.visibility = View.VISIBLE
                it.tv_status.text = ERROR_STATE
                it.pb_load.visibility = View.GONE
                it.ivBtn_close.setOnClickListener { _ ->
                    this.removeView(it)
                }
                it.ivBtn_refresh.setOnClickListener {

                }
            }
        }
        SUCCESS_STATE -> {
            stateLayout?.let {
                this.removeView(it)
            }
        }

    }
}
