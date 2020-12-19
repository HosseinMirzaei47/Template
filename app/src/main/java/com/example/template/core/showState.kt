package com.example.template.core

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.template.BaseLiveTask
import com.example.template.R
import com.example.template.core.util.LiveTask
import kotlinx.android.synthetic.main.layout_state.view.*

const val LOAD_STATE = "loading"
const val ERROR_STATE = "error"
const val SUCCESS_STATE = "success"

interface State {
    fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    )
}

class LoadingState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    ) {
        stateLayout?.let {
            it.apply {
                tv_status.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.fade_out_repeatition
                    )
                )
                layoutParams.height = 150
                ivBtn_refresh.visibility = View.INVISIBLE
                pb_load.visibility = View.VISIBLE
                tv_status.text = LOAD_STATE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close.visibility = View.VISIBLE
                    ivBtn_close.setOnClickListener { _ ->
                        result.cancel()
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        parent.removeView(it)
                    }
                } else ivBtn_close.visibility = View.INVISIBLE
            }

        }
    }
}

class ErrorState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    ) {
        stateLayout?.let {
            it.tv_status.clearAnimation()

            it.ivBtn_close.visibility = View.VISIBLE
            it.ivBtn_close.setOnClickListener { _ ->
                it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                parent.removeView(it)
            }
            if ((result as BaseLiveTask<*>).retryable) {
                it.ivBtn_refresh.visibility = View.VISIBLE
                it.ivBtn_refresh.setOnClickListener {
                    result.retry()
                }
            } else it.ivBtn_refresh.visibility = View.INVISIBLE

            it.tv_status.text = ERROR_STATE
            it.pb_load.visibility = View.GONE
        }
    }
}

class SuccessState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    ) {
        stateLayout?.let {
            parent.removeView(it)
        }
    }
}