package com.spartancookie.hermeslogger.debugToaster

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
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.ui.setLogIcon
import com.spartancookie.hermeslogger.utils.NO_RES
import com.spartancookie.hermeslogger.utils.copyToClipboard
import java.lang.ref.WeakReference


/**
 * Duration between toasts
 */
private const val CooldownDuration = 500
private const val AnimationDuration = 450
private const val ActiveToastAlpha = 1f
private const val InactiveToastAlpha = 0f

internal class DebugToast private constructor(activity: Activity, private val dataHolder: LogDataHolder) : FrameLayout(activity) {

    private lateinit var rootLayout: View
    private val iconIV by lazy { rootLayout.findViewById<ImageView>(R.id.icon) }
    private val textTV by lazy { rootLayout.findViewById<TextView>(R.id.text) }
    private val copyIV by lazy { rootLayout.findViewById<ImageView>(R.id.copy) }

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
        rootLayout = LayoutInflater.from(activity).inflate(R.layout.toast_layout, this, true)

        // Add the view onto the container fetched above
        activity.runOnUiThread {
            container.addView(rootLayout, container.childCount, buildLayoutParams())
        }

        with(rootLayout) {
            // Start toast fade in
            fade(this, true)
            // Set copy to clipboard action to on view click
            setOnClickListener { activityReference.get()?.run { copyToClipboard(this, dataHolder) } }
            // Schedule toast's fade out and description
            scheduleDestruction(this)
        }

        fillContent()
    }

    private fun fillContent() {
        iconIV.setLogIcon(dataHolder.type, if (dataHolder.type == LogType.Debug) R.color.white else NO_RES)
        copyIV.visibility = dataHolder.extraInfo?.run { GONE } ?: VISIBLE

        with(textTV) {
            text = dataHolder.message
            visibility = if (dataHolder.message.isEmpty()) GONE else VISIBLE
        }
    }

    private fun scheduleDestruction(view: View) {
        Handler(Looper.getMainLooper()).apply {
            postDelayed(
                {
                    fade(view, false)
                    Toaster.refreshHasToastsLiveData()
                },
                dataHolder.duration - AnimationDuration - CooldownDuration
            )
        }
    }

    private fun buildLayoutParams() = LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    ).apply {
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