package com.example.template.core.bindingadapter

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.template.R
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.BaseLiveTask
import com.example.template.core.livatask.LiveTask
import kotlinx.android.synthetic.main.loading_blur_circular.view.*
import kotlinx.android.synthetic.main.loading_bouncing.view.*
import kotlinx.android.synthetic.main.loading_circular.view.*
import kotlinx.android.synthetic.main.loading_indicator.view.*
import kotlinx.android.synthetic.main.loading_linear.view.*
import kotlinx.android.synthetic.main.loading_sandy_clock.view.*
import kotlin.coroutines.cancellation.CancellationException

enum class ProgressType {
    INDICATOR, LINEAR, SANDY_CLOCK, CIRCULAR, BOUNCING, BLUR_CIRCULAR
}

enum class Theme {
    DARK, LIGHT
}

fun ProgressType.loading() =
    when (this) {
        ProgressType.INDICATOR -> IndicatorLoading()
        ProgressType.SANDY_CLOCK -> SandyClockLoading()
        ProgressType.LINEAR -> LinearLoading()
        ProgressType.CIRCULAR -> CircularLoading()
        ProgressType.BOUNCING -> BouncingLoading()
        ProgressType.BLUR_CIRCULAR -> BlurCircularLoading()
    }

interface State {
    fun loading(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    )

    fun error(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    )

    fun success(
        stateLayout: View?,
        parent: ViewGroup,
        result: LiveTask<*>,
    ) {
        stateLayout?.let {
            parent.removeView(it)
        }
    }
}

class IndicatorLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_indicator.visibility = View.INVISIBLE
                progressBar_indicator.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_indicator.visibility = View.VISIBLE
                    ivBtn_close_indicator.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_indicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as LiveTaskResult.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    /*val red = Color.parseColor("#E91E63")
                    val white = Color.parseColor("#ffffff")
                    val border = GradientDrawable()
                    border.setColor(red) //white background
                    border.setStroke(0, white) //black border with full opacity
                    it.background = border
                    tv_error_indicator.setTextColor(white)
                    ivBtn_close_indicator.setColorFilter(white)
                    iv_retry_indicator.setColorFilter(white)*/

                    ivBtn_close_indicator.visibility = View.VISIBLE
                    cl_error_indicator.visibility = View.VISIBLE
                    ivBtn_close_indicator.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        tv_error_indicator.visibility = View.VISIBLE
                        cl_error_indicator.setOnClickListener {
                            result.retry()
                        }
                    } else tv_error_indicator.visibility = View.INVISIBLE
                    progressBar_indicator.visibility = View.GONE
                }

            }
        }
    }
}

class SandyClockLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_sandy_clock.visibility = View.GONE
                progressBar_sandy_clock.visibility = View.VISIBLE
                tv_loading_sandy_clock.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_sandy_clock.visibility = View.VISIBLE
                    ivBtn_close_sandy_clock.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_sandy_clock.visibility = View.INVISIBLE
            }
        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as LiveTaskResult.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    ivBtn_close_sandy_clock.visibility = View.VISIBLE
                    cl_error_sandy_clock.visibility = View.VISIBLE
                    ivBtn_close_sandy_clock.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        cl_container_sandy_clock.visibility = View.VISIBLE
                        cl_error_sandy_clock.setOnClickListener {
                            result.retry()
                        }
                    } else cl_container_sandy_clock.visibility = View.INVISIBLE
                    progressBar_sandy_clock.visibility = View.GONE
                    tv_loading_sandy_clock.visibility = View.GONE
                }

            }
        }
    }
}

class LinearLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_linear.visibility = View.GONE
                progressBar_linear.visibility = View.VISIBLE
                tv_loading_linear.visibility = View.VISIBLE
                progressBar_linear_sandy_clock.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_linear.visibility = View.VISIBLE
                    ivBtn_close_linear.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_linear.visibility = View.INVISIBLE
            }

        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as LiveTaskResult.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    ivBtn_close_linear.visibility = View.VISIBLE
                    cl_error_linear.visibility = View.VISIBLE
                    ivBtn_close_linear.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context,
                            R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        cl_container_linear.visibility = View.VISIBLE
                        cl_error_linear.setOnClickListener {
                            result.retry()
                        }
                    } else cl_container_linear.visibility = View.INVISIBLE
                    progressBar_linear_sandy_clock.visibility = View.GONE
                    progressBar_linear.visibility = View.GONE
                    tv_loading_linear.visibility = View.GONE
                }
            }
        }
    }
}

class CircularLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_circular.visibility = View.GONE
                progressBar_circular.visibility = View.VISIBLE
                tv_loading_circular.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_circular.visibility = View.VISIBLE
                    ivBtn_close_circular.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_circular.visibility = View.INVISIBLE
            }
        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as LiveTaskResult.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    ivBtn_close_circular.visibility = View.VISIBLE
                    cl_error_circular.visibility = View.VISIBLE
                    ivBtn_close_circular.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        cl_container_circular.visibility = View.VISIBLE
                        cl_error_circular.setOnClickListener {
                            result.retry()
                        }
                    } else cl_container_circular.visibility = View.INVISIBLE
                    progressBar_circular.visibility = View.GONE
                    tv_loading_circular.visibility = View.GONE
                }

            }
        }
    }
}

class BouncingLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_bouncing.visibility = View.GONE
                progressBar_bouncing.visibility = View.VISIBLE
                tv_loading_bouncing.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_bouncing.visibility = View.VISIBLE
                    ivBtn_close_bouncing.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_bouncing.visibility = View.INVISIBLE
            }
        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as LiveTaskResult.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    ivBtn_close_bouncing.visibility = View.VISIBLE
                    cl_error_bouncing.visibility = View.VISIBLE
                    ivBtn_close_bouncing.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        cl_container_bouncing.visibility = View.VISIBLE
                        cl_error_bouncing.setOnClickListener {
                            result.retry()
                        }
                    } else cl_container_bouncing.visibility = View.INVISIBLE
                    progressBar_bouncing.visibility = View.GONE
                    tv_loading_bouncing.visibility = View.GONE
                }

            }
        }
    }
}

class BlurCircularLoading : State {
    override fun loading(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                cl_error_blur_circular.visibility = View.GONE
                progressBar_blur_circular.visibility = View.VISIBLE
//                tv_loading_blur_circular.visibility = View.VISIBLE
                if ((result as BaseLiveTask<*>).cancelable) {
                    ivBtn_close_blur_circular.visibility = View.VISIBLE
                    ivBtn_close_blur_circular.setOnClickListener { _ ->
                        result.cancel()
                    }
                } else ivBtn_close_blur_circular.visibility = View.INVISIBLE
            }
        }
    }

    override fun error(stateLayout: View?, parent: ViewGroup, result: LiveTask<*>) {
        stateLayout?.let {
            it.apply {
                if ((result.result() as Result.Error).exception is CancellationException) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    tag = null
                    parent.removeView(it)
                } else {
                    ivBtn_close_blur_circular.visibility = View.VISIBLE
                    cl_error_blur_circular.visibility = View.VISIBLE
                    ivBtn_close_blur_circular.setOnClickListener { _ ->
                        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                        tag = null
                        parent.removeView(it)
                    }
                    if ((result as BaseLiveTask<*>).retryable) {
                        cl_container_blur_circular.visibility = View.VISIBLE
                        cl_error_blur_circular.setOnClickListener {
                            result.retry()
                        }
                    } else cl_container_blur_circular.visibility = View.INVISIBLE
                    progressBar_blur_circular.visibility = View.GONE
//                    tv_loading_blur_circular.visibility = View.GONE
                }

            }
        }
    }
}