package com.example.template.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import com.example.template.R
import com.example.template.core.util.CoroutineLiveTask
import kotlinx.android.synthetic.main.layout_state.view.*

private const val LOAD_STATE = "loading"
private const val ERROR_STATE = "error"
private const val SUCCESS_STATE = "success"

@BindingAdapter("reactToResult")
fun <T> View.reactToResult(result: CoroutineLiveTask<T>) {
    when (result.value) {
        is Result.Success -> {
            var stateLayout = situationOfStateLayout(this).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this).first
            }
            val parent = situationOfStateLayout(this).second
            showLoadingState(SUCCESS_STATE, stateLayout, parent, result)
        }
        is Result.Loading -> {
            var stateLayout = situationOfStateLayout(this).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this).first
            }
            val parent = situationOfStateLayout(this).second
            showLoadingState(LOAD_STATE, stateLayout, parent, result)
        }
        is Result.Error -> {
            var stateLayout = situationOfStateLayout(this).first
            if (stateLayout == null) {
                this.tag = null
                stateLayout = situationOfStateLayout(this).first
            }
            val parent = situationOfStateLayout(this).second
            showLoadingState(ERROR_STATE, stateLayout, parent, result)
        }
    }
}

fun situationOfStateLayout(view: View): Pair<View, Any> {
    when (view) {
        is ConstraintLayout -> {
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
                val parent = view.parent as ConstraintLayout
                if (view.tag == null) {
                    val stateLayout = inflateStateLayoutAndSetID(parent)
                    setConstraintForStateLayout(parent, stateLayout, view)
                    view.tag = stateLayout.id
                }
                return Pair(parent.getViewById(view.tag as Int), parent)
            } else {
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
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.END,
        ConstraintSet.PARENT_ID,
        ConstraintSet.END
    )
    constraintSet.connect(
        stateLayout.id,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START
    )
    parent.addView(stateLayout)
    constraintSet.applyTo(parent)
}

fun showLoadingState(
    state: String,
    stateLayout: View?,
    parent: Any,
    result: CoroutineLiveTask<*>
) {
    parent as ViewGroup

    when (state) {

        LOAD_STATE -> {
            stateLayout?.let {
                println("mamad0 load")
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
                it.ivBtn_close.setOnClickListener { _ ->
                    result.cancel()
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }
            }
        }
        ERROR_STATE -> {
            println("mamad1 error")
            stateLayout?.let {
                it.tv_status.clearAnimation()
                it.ivBtn_refresh.visibility = View.VISIBLE
                it.tv_status.text = ERROR_STATE
                it.pb_load.visibility = View.GONE
                it.ivBtn_close.setOnClickListener { _ ->
                    it.startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.fade_out))
                    parent.removeView(it)
                }
                it.ivBtn_refresh.setOnClickListener {
                    result.retry()
                }
            }
        }
        SUCCESS_STATE -> {
            println("mamad2 success")
            stateLayout?.let {
                parent.removeView(it)
            }
        }

    }
}

@BindingAdapter("enablebutton")
fun <T> Button.enablebutton(result: CoroutineLiveTask<T>) {
    when (result.value) {
        is Result.Loading -> {
            this.isEnabled = false
        }
        is Result.Success -> {
            this.isEnabled = true

        }
        is Result.Error -> {
            this.text = "errore"
            this.isEnabled = false
        }
    }
}