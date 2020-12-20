package com.example.template.core

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.template.BaseLiveTask
import com.example.template.R
import com.example.template.core.util.LiveTask
import kotlinx.android.synthetic.main.loading_indicator.view.*
import kotlinx.android.synthetic.main.loading_linear.view.*
import kotlinx.android.synthetic.main.loading_sandy_clock.view.*


const val LOAD_STATE = "loading"
const val ERROR_STATE = "error"
const val SUCCESS_STATE = "success"

interface State {
    fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
        type: Type,
    )
}

class LoadingState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
        type: Type,
    ) {
        if (type == Type.INDICATOR) {
            stateLayout?.let {
                it.apply {
                    layoutParams.height = 150
                    tv_error_indicator.visibility = View.INVISIBLE
                    progressBar_indicator.visibility = View.VISIBLE
                    if ((result as BaseLiveTask<*>).cancelable) {
                        ivBtn_close_indicator.visibility = View.VISIBLE
                        ivBtn_close_indicator.setOnClickListener { _ ->
                            result.cancel()
                            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                            parent.removeView(it)
                        }
                    } else ivBtn_close_indicator.visibility = View.INVISIBLE
                }
            }
        } else if (type == Type.SANDY_CLOCK) {
            stateLayout?.let {
                it.apply {
                    layoutParams.height = 150
                    tv_error_sandy_clock.visibility = View.INVISIBLE
                    progressBar_sandy_clock.visibility = View.VISIBLE
                    tv_loading_sandy_clock.visibility = View.VISIBLE
                    if ((result as BaseLiveTask<*>).cancelable) {
                        ivBtn_close_sandy_clock.visibility = View.VISIBLE
                        ivBtn_close_sandy_clock.setOnClickListener { _ ->
                            result.cancel()
                            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                            parent.removeView(it)
                        }
                    } else ivBtn_close_sandy_clock.visibility = View.INVISIBLE
                }

            }
        } else if (type == Type.LINEAR) {
            stateLayout?.let {
                it.apply {
                    layoutParams.height = 150
                    tv_error_linear.visibility = View.INVISIBLE
                    progressBar_linear.visibility = View.VISIBLE
                    tv_loading_linear.visibility = View.VISIBLE
                    progressBar_linear_sandy_clock.visibility = View.VISIBLE
                    if ((result as BaseLiveTask<*>).cancelable) {
                        ivBtn_close_linear.visibility = View.VISIBLE
                        ivBtn_close_linear.setOnClickListener { _ ->
                            result.cancel()
                            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                            parent.removeView(it)
                        }
                    } else ivBtn_close_linear.visibility = View.INVISIBLE
                }

            }
        }
    }
}

class ErrorState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
        type: Type,
    ) {
        if (type == Type.INDICATOR) {
            stateLayout?.let {
                it.ivBtn_close_indicator.visibility = View.VISIBLE
                it.tv_error_indicator.visibility = View.VISIBLE
                it.ivBtn_close_indicator.setOnClickListener { _ ->
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }
                if ((result as BaseLiveTask<*>).retryable) {
                    it.tv_error_indicator.visibility = View.VISIBLE
                    it.tv_error_indicator.setOnClickListener {
                        result.retry()
                    }
                } else it.tv_error_indicator.visibility = View.INVISIBLE
                it.progressBar_indicator.visibility = View.GONE
            }
        } else if (type == Type.SANDY_CLOCK) {
            stateLayout?.let {
                it.ivBtn_close_sandy_clock.visibility = View.VISIBLE
                it.tv_error_sandy_clock.visibility = View.VISIBLE
                it.ivBtn_close_sandy_clock.setOnClickListener { _ ->
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }
                if ((result as BaseLiveTask<*>).retryable) {
                    it.cl_container_sandy_clock.visibility = View.VISIBLE
                    it.tv_error_sandy_clock.setOnClickListener {
                        result.retry()
                    }
                } else it.cl_container_sandy_clock.visibility = View.INVISIBLE
                it.progressBar_sandy_clock.visibility = View.GONE
                it.tv_loading_sandy_clock.visibility = View.GONE
            }
        } else if (type == Type.LINEAR) {
            stateLayout?.let {
                it.ivBtn_close_linear.visibility = View.VISIBLE
                it.tv_error_linear.visibility = View.VISIBLE
                it.ivBtn_close_linear.setOnClickListener { _ ->
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }
                if ((result as BaseLiveTask<*>).retryable) {
                    it.cl_container_linear.visibility = View.VISIBLE
                    it.tv_error_linear.setOnClickListener {
                        result.retry()
                    }
                } else it.cl_container_linear.visibility = View.INVISIBLE
                it.progressBar_linear_sandy_clock.visibility = View.GONE
                it.progressBar_linear.visibility = View.GONE
                it.tv_loading_linear.visibility = View.GONE
            }
        }
    }
}

class SuccessState : State {
    override fun handleState(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
        type: Type,
    ) {
        stateLayout?.let {
            parent.removeView(it)
        }
    }
}