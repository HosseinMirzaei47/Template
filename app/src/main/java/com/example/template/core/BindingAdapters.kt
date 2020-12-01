package com.example.template.core

import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.template.R
import com.example.template.core.util.CoroutineLiveTask
import kotlinx.android.synthetic.main.layout_state.view.ivBtn_close
import kotlinx.android.synthetic.main.layout_state.view.ivBtn_refresh
import kotlinx.android.synthetic.main.layout_state.view.pb_load
import kotlinx.android.synthetic.main.layout_state.view.tv_status

private const val LOAD_STATE = "loading"
private const val ERROR_STATE = "error"
private const val SUCCESS_STATE = "success"
var hashMap: HashMap<Int, View> = HashMap()

@BindingAdapter("reactToResult")
fun <T> View.reactToResult(result: CoroutineLiveTask<T>) {
    this as ConstraintLayout
    if(this.tag == null){
        val stateLayout = LayoutInflater.from(context).inflate(R.layout.layout_state, this, false)
        stateLayout.id = View.generateViewId()
        this.addView(stateLayout)
        this.tag = stateLayout.id
    }
    val stateLayout = this.getViewById(this.tag as Int)
    when (result.value) {
        is Result.Success -> {
            showLoadingState(SUCCESS_STATE, stateLayout, result)
        }
        is Result.Loading -> {
            showLoadingState(LOAD_STATE, stateLayout, result)
        }
        is Result.Error -> {
            showLoadingState(ERROR_STATE, stateLayout, result)
        }
    }
}

fun View.showLoadingState(state: String, stateLayout: View?, result: CoroutineLiveTask<*>) {
    this as ConstraintLayout
    when (state) {
        LOAD_STATE -> {
            stateLayout?.let {
                it.layoutParams.height = 60
                it.ivBtn_refresh.visibility = View.INVISIBLE
                it.pb_load.visibility = View.VISIBLE
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
                    result.retry()
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
