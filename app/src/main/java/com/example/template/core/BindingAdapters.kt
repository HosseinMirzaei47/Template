package com.example.template.core

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.template.BaseLiveTask
import com.example.template.R
import kotlinx.android.synthetic.main.layout_state.view.*

private const val LOAD_STATE = "loading"
private const val ERROR_STATE = "error"
private const val SUCCESS_STATE = "success"

@BindingAdapter("reactToTask")
fun <T> ViewGroup.reactToTask(result: BaseLiveTask<Any>) {


    when (result.value) {
        is Result.Success -> {
            var stateLayout = situationOfStateLayout(this, result).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this, result).first
            }
            val parent = situationOfStateLayout(this, result).second
            showLoadingState(SUCCESS_STATE, stateLayout, parent, result)
        }
        is Result.Loading -> {
            var stateLayout = situationOfStateLayout(this, result).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this, result).first
            }
            val parent = situationOfStateLayout(this, result).second
            showLoadingState(LOAD_STATE, stateLayout, parent, result)
        }
        is Result.Error -> {
            var stateLayout = situationOfStateLayout(this, result).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this, result).first
            }
            val parent = situationOfStateLayout(this, result).second
            showLoadingState(ERROR_STATE, stateLayout, parent, result)
        }
    }
}

fun situationOfStateLayout(view: View, result: BaseLiveTask<Any>): Pair<View, Any> {
    when (view) {
        is ConstraintLayout -> {
            Log.d("parent", "ConstraintLayout")
            if (view.tag == null) {
                val stateLayout = inflateStateLayoutAndSetID(view)
                //    all childes of parent must have id to clone in constraint set
                //    setConstraintForStateLayout(view, stateLayout, view)
                view.addView(stateLayout)
                view.tag = stateLayout.id
            }
            return Pair(view.getViewById(view.tag as Int), view)
        }
        else -> {
            if (view.parent is ConstraintLayout) {
                Log.d("parent", "parent ConstraintLayout")
                val parent = view.parent as ConstraintLayout
                if (view.tag == null) {
                    val stateLayout = inflateStateLayoutAndSetID(parent)
                    setConstraintForStateLayout(parent, stateLayout, view)
                    view.tag = stateLayout.id
                }
                return Pair(parent.getViewById(view.tag as Int), parent)
            } else {
                Log.d("parent", "Otheeeeeeeeeer")
                val parent = view.parent as ViewGroup

                if (view.tag == null) {
                    val stateLayout = inflateStateLayoutAndSetID(parent)
                    parent.addView(stateLayout)
                    view.tag = stateLayout.id
                }
                return Pair(parent.findViewById(view.tag as Int), parent)
            }
        }
    }
}

private fun inflateStateLayoutAndSetID(view: ViewGroup): View {
    val stateLayout = LayoutInflater.from(view.context)
        .inflate(R.layout.layout_state, view, false)
    stateLayout.id = View.generateViewId()
    return stateLayout
}

private fun setConstraintForStateLayout(
    parent: ConstraintLayout,
    stateLayout: View,
    view: View
) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(parent)
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.TOP,
        view.id,
        ConstraintSet.TOP
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.BOTTOM,
        view.id,
        ConstraintSet.BOTTOM
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.END,
        view.id,
        ConstraintSet.END
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.START,
        view.id,
        ConstraintSet.START
    )
    parent.addView(stateLayout)
    constraintSet.applyTo(parent)
}

fun showLoadingState(
    state: String,
    stateLayout: View?,
    parent: Any,
    result: BaseLiveTask<Any>
) {
    parent as ViewGroup

    when (state) {

        LOAD_STATE -> {
            stateLayout?.let {
                it.tv_status.startAnimation(
                    AnimationUtils.loadAnimation(
                        it.context,
                        R.anim.fade_out_repeatition
                    )
                )
                it.layoutParams.height = 60
                it.ivBtn_refresh.visibility = View.INVISIBLE
                it.pb_load.visibility = View.VISIBLE
                it.tv_status.text = LOAD_STATE
                if (result.cancelable) {
                    it.ivBtn_close.visibility = View.VISIBLE
                    it.ivBtn_close.setOnClickListener { _ ->
                        result.cancel()
                        it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                        parent.removeView(it)
                    }
                } else it.ivBtn_close.visibility = View.INVISIBLE

            }
        }
        ERROR_STATE -> {
            stateLayout?.let {
                it.tv_status.clearAnimation()

                it.ivBtn_close.visibility = View.VISIBLE
                it.ivBtn_close.setOnClickListener { _ ->
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }


                if (result.retryable) {
                    it.ivBtn_refresh.visibility = View.VISIBLE
                    it.ivBtn_refresh.setOnClickListener {
                        result.retry()
                    }
                } else it.ivBtn_refresh.visibility = View.INVISIBLE

                it.tv_status.text = ERROR_STATE
                it.pb_load.visibility = View.GONE


            }
        }
        SUCCESS_STATE -> {
            stateLayout?.let {
                parent.removeView(it)
            }
        }
    }
}

@BindingAdapter("visibleOnLoading")
fun View.visibleOnLoading(result: BaseLiveTask<Any>?) {
    this.isVisible = Result.Loading == result?.value
}

@BindingAdapter("disableOnLoading")
fun Button.disableOnLoading(result: BaseLiveTask<Any>?) {
    this.isEnabled = Result.Loading != result?.value
}