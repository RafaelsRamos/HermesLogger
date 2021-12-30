package com.spartancookie.hermeslogger.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class MarginItemDecoration constructor(
    private val topMargin: Int = 0,
    private val bottomMargin: Int = 0,
    private val rightMargin: Int = 0,
    private val leftMargin: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = topMargin
            left = leftMargin
            right = rightMargin
            bottom = bottomMargin
        }
    }

    companion object {

        fun withSameMargins(margin: Int) = MarginItemDecoration(margin, margin, margin, margin)

        fun withMargins(top: Int, bottom: Int, right: Int, left: Int) = MarginItemDecoration(top, bottom, right, left)

    }
}