package com.example.template.core

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    val stateLayout = situationOfStateLayout(this).first
    val parent = situationOfStateLayout(this).second
    when (result.value) {
        is Result.Success -> {
            showLoadingState(SUCCESS_STATE, stateLayout, parent, result)
        }
        is Result.Loading -> {
            showLoadingState(LOAD_STATE, stateLayout, parent, result)
        }
        is Result.Error -> {
            showLoadingState(ERROR_STATE, stateLayout, parent, result)
        }
    }
}

fun situationOfStateLayout(view: View): Pair<View, Any> {
    when (view) {
        is ConstraintLayout -> {
            if (view.tag == null) {
                val stateLayout = inflateStateLayoutAndSetID(view)
                Log.d("view : ", view.id.toString())
                Log.d("stateLayout : ", stateLayout.id.toString())
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
    Log.d("mamad", "setConstraintForStateLayout: ")
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
                it.layoutParams.height = 60
                it.ivBtn_refresh.visibility = View.INVISIBLE
                it.pb_load.visibility = View.VISIBLE
                it.tv_status.text = LOAD_STATE
                it.ivBtn_close.setOnClickListener { _ ->
                    parent.removeView(it)
                }
            }
        }
        ERROR_STATE -> {
            stateLayout?.let {
                it.ivBtn_refresh.visibility = View.VISIBLE
                it.tv_status.text = ERROR_STATE
                it.pb_load.visibility = View.GONE
                it.ivBtn_close.setOnClickListener { _ ->
                    parent.removeView(it)
                }
                it.ivBtn_refresh.setOnClickListener {
                    result.retry()
                }
            }
        }
        SUCCESS_STATE -> {
            stateLayout?.let {
                parent.removeView(it)
            }
        }

    }
}
