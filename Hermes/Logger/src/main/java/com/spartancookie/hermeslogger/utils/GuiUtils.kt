package com.spartancookie.hermeslogger.utils

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.spartancookie.hermeslogger.R
import kotlin.math.roundToInt


private const val COPY_TO_CLIPBOARD_ANIMATION_DURATION = 250L

/**
 * Copy to clipboard color animation
 * Animate [view] color from [R.color.white] to [R.color.copyFinalColor] and back to [R.color.white]
 * throughout [COPY_TO_CLIPBOARD_ANIMATION_DURATION] milliseconds
 */
fun animateCopyToClipboardColor(view: View) {
    val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) { }

        override fun onAnimationCancel(animator: Animator) { }

        override fun onAnimationRepeat(animator: Animator) { }

        override fun onAnimationEnd(animator: Animator) {
            animateColor(view, R.color.copyFinalColor, R.color.white, COPY_TO_CLIPBOARD_ANIMATION_DURATION / 2)
        }
    }
    animateColor(view, R.color.white, R.color.copyFinalColor, COPY_TO_CLIPBOARD_ANIMATION_DURATION / 2, animationListener)
}

/**
 * Animate [view] color from [fromColorRes] to [toColorRes] throughout [COPY_TO_CLIPBOARD_ANIMATION_DURATION] milliseconds.
 * @param view          View that will be animated
 * @param fromColorRes  Initial color resource
 * @param toColorRes    Final color resource
 * @param duration      Animation duration
 * @param listener      Animation listener
 */
fun animateColor(view: View, fromColorRes: Int, toColorRes: Int, duration: Long, listener: Animator.AnimatorListener? = null) {
    val context = view.context
    val colorFrom = ContextCompat.getColor(context, fromColorRes)
    val colorTo = ContextCompat.getColor(context, toColorRes)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = duration
    colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
    listener?.run { colorAnimation.addListener(this) }
    colorAnimation.start()
}

/**
 * From DP to pixels
 * @return Number of pixels in [this] DPs
 */
fun Float.fromDPToPx(): Int {
    val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return px.roundToInt()
}