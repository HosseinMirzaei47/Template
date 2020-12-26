package com.example.template.core.bindingadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.template.R


interface Situation {
    fun inflateStateLayout(view: ViewGroup, layoutId: Int): View {
        Log.d("execute", "inflateStateLayout: $view , $layoutId")
        val stateLayout = LayoutInflater.from(view.context)
            .inflate(layoutId, view, false)
        stateLayout.id = View.generateViewId()
//        stateLayout.translationZ = 10F
        return stateLayout
    }

    fun execute(): Pair<View, Any>
}

class IsConstrainLayout(private val parent: ConstraintLayout, val view: View, val layout: Int) :
    Situation {
    override fun execute(): Pair<View, Any> {
        Log.d("execute", "execute: isconstraint")
        if (view.tag == null) {
            val stateLayout = inflateStateLayout(parent, layout)
            setConstraintForStateLayout(stateLayout)
//            stateLayout.translationZ = 10F
//            parent.bringChildToFront(stateLayout)
            stateLayout.bringToFront()
            view.tag = stateLayout.id
        }
        return Pair(parent.getViewById(view.tag as Int), parent)
    }

    private fun setConstraintForStateLayout(
        stateLayout: View,
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
//        constraintSet.constrainMaxHeight(stateLayout.id, view.layoutParams.height - 10)
//        constraintSet.constrainHeight(stateLayout.id, view.layoutParams.height - 10)
//        constraintSet.constrainMinHeight(stateLayout.id, view.layoutParams.height - 10)
//        constraintSet.constrainDefaultHeight(stateLayout.id, view.layoutParams.height - 10)
        parent.addView(stateLayout)
        constraintSet.applyTo(parent)
    }
}

class IsNotConstrainLayout(val view: View, val layout: Int) : Situation {
    override fun execute(): Pair<View, Any> {
        Log.d("execute", "execute: IsNotConstrainLayout")
        val parent = view.parent as ViewGroup
        if (view.tag == null) {
            val stateLayout = inflateStateLayout(parent, layout)
            parent.addView(stateLayout)
            stateLayout.translationZ = 10F
            stateLayout.bringToFront()
            parent.bringChildToFront(stateLayout)

            view.tag = stateLayout.id
        }
        return Pair(parent.findViewById(view.tag as Int), parent)
    }
}

class SituationFactory() {
    fun createSituation(view: View, layout: Int): Situation =
        when (view) {
            is ConstraintLayout ->
                IsConstrainLayout(view, view, layout)

            else -> {
                if (view.parent is ConstraintLayout)
                    IsConstrainLayout(view.parent as ConstraintLayout, view, layout)
                else
                    IsNotConstrainLayout(view, layout)
            }
        }

    fun executeState(
        view: View,
        progressType: ProgressType?,
        layout: Int?,
    ): Pair<View, Any> {
        val loadingLayout: Int = layout ?: when (progressType) {
            ProgressType.INDICATOR -> R.layout.loading_indicator
            ProgressType.SANDY_CLOCK -> R.layout.loading_sandy_clock
            ProgressType.LINEAR -> R.layout.loading_linear
            ProgressType.CIRCULAR -> R.layout.loading_circular
            ProgressType.BOUNCING -> R.layout.loading_bouncing
            ProgressType.BLUR_CIRCULAR -> R.layout.loading_blur_circular
            else -> R.layout.loading_sandy_clock
        }
        Log.d("layout", "reactToTask: $loadingLayout")
        val situation = createSituation(view, loadingLayout).execute()
        var stateLayout = situation.first
        if (stateLayout == null) {
            view.tag = null
            stateLayout = createSituation(view, loadingLayout).execute().first
        }
        val parent = situation.second
        return Pair(stateLayout, parent)
    }

}