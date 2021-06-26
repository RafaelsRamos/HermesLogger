package com.rafaelsramos.hermes.debugToaster

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.databinding.ToastLayoutBinding
import com.rafaelsramos.hermes.models.LogDataHolder
import com.rafaelsramos.hermes.utils.copyToClipboard
import java.lang.ref.WeakReference


/**
 * Duration between toasts
 */
private const val CooldownDuration = 500
private const val AnimationDuration = 450
private const val ActiveToastAlpha = 1f
private const val InactiveToastAlpha = 0f

private const val VerticalMargin = 100
private const val HorizontalMargin = 15

internal class DebugToast private constructor(activity: Activity, private val dataHolder: LogDataHolder) : FrameLayout(activity) {

    private var binding: ToastLayoutBinding
    private var mGravity = Gravity.BOTTOM

    private val activityReference = WeakReference(activity)

    private val horizontalMargins = activity.resources.getDimension(R.dimen.toast_horizontal_margins).toInt()
    private val downMargin = activity.resources.getDimension(R.dimen.toast_bottom_margin).toInt()
    private val upMargin = activity.resources.getDimension(R.dimen.toast_top_margin).toInt()

    companion object {
        @JvmStatic
        fun show(@NonNull activity: Activity, dataHolder: LogDataHolder, gravity: Int = Gravity.BOTTOM) = DebugToast(activity, dataHolder). apply { mGravity = gravity }
    }

    init {
        // Get activity's ViewGroup, to launch our toast
        val container = activity.findViewById<ViewGroup>(android.R.id.content)

        // Inflate the view
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.toast_layout, this, false)

        // Add the view onto the container fetched above
        container.addView(binding.root, container.childCount, buildLayoutParams())

        binding.logDataHolder = dataHolder

        binding.root.let { viewRoot ->
            // Start toast fade in
            fade(viewRoot, true)
            // Set copy to clipboard action to on view click
            viewRoot.setOnClickListener {
                activityReference.get()?.run { copyToClipboard(this, dataHolder) }
            }
            // Schedule toast's fade out and description
            scheduleDestruction(viewRoot)
        }
    }

    private fun scheduleDestruction(view: View) {
        Handler(Looper.getMainLooper()).apply {
            postDelayed( { fade(view, false) }, dataHolder.duration - AnimationDuration - CooldownDuration)
        }
    }

    private fun buildLayoutParams() = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
        gravity = mGravity or Gravity.CENTER_HORIZONTAL
        when (mGravity) {
            Gravity.BOTTOM -> setMargins(horizontalMargins, 0, horizontalMargins, downMargin)
            Gravity.TOP -> setMargins(horizontalMargins, upMargin, horizontalMargins, 0)
        }
    }

    private fun fade(view: View, isFadeIn: Boolean) {
        val animation = getAlphaAnimation(isFadeIn).apply {
            duration = AnimationDuration.toLong()
            fillAfter = true
            if (!isFadeIn) {
                setAnimationListener(getFadeOutAnimationListener(view))
            }
        }
        view.startAnimation(animation)
    }

    private fun getFadeOutAnimationListener(view: View) = object: Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) { }

        override fun onAnimationRepeat(animation: Animation?) { }

        override fun onAnimationEnd(animation: Animation?) {
            val parentViewGroup = (view.parent as? ViewGroup)
            parentViewGroup?.removeView(view)
        }
    }

    private fun getAlphaAnimation(isFadeIn: Boolean) = AlphaAnimation(getInitialAlpha(isFadeIn), getFinalAlpha(isFadeIn))

    private fun getInitialAlpha(isFadeIn: Boolean) = if (isFadeIn) InactiveToastAlpha else ActiveToastAlpha

    private fun getFinalAlpha(isFadeIn: Boolean) = if (isFadeIn) ActiveToastAlpha else InactiveToastAlpha
}