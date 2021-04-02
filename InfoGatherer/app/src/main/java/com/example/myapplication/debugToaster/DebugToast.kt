package com.example.myapplication.debugToaster

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.copyToClipboard

/**
 * Duration between toasts
 */
private const val CooldownDuration = 500
private const val AnimationDuration = 450
private const val ActiveToastAlpha = 1f
private const val InactiveToastAlpha = 0f

private const val VerticalMargin = 100
private const val HorizontalMargin = 15

private const val DefaultResource = R.layout.toast_layout

class DebugToast private constructor(private val activity: Activity, private val dataHolder: LogDataHolder) : FrameLayout(activity), View.OnClickListener {

    var mGravity = Gravity.BOTTOM

    companion object {

        @JvmStatic
        fun show(@NonNull activity: Activity, dataHolder: LogDataHolder, gravity: Int = Gravity.BOTTOM) : DebugToast {
            val toast = DebugToast(activity, dataHolder)
            toast.mGravity = gravity
            return toast
        }

    }

    init {
        val container = activity.findViewById<ViewGroup>(android.R.id.content)
        val layoutParams = buildLayoutParams()
        val view: View = LayoutInflater.from(activity).inflate(DefaultResource, this, true)
        container.addView(view, container.childCount, layoutParams)

        val text: TextView = view.findViewById(R.id.text)
        val icon: ImageView = view.findViewById(R.id.icon)

        if (dataHolder.extraInfo == null) {
            view.findViewById<View>(R.id.copy).visibility = View.GONE
        }

        icon.setImageDrawable(ContextCompat.getDrawable(activity, dataHolder.type.drawableResource))
        text.text = dataHolder.msg

        fade(view, true)
        view.setOnClickListener(this)

        scheduleDestruction(view, container)
    }

    /**
     * Schedule view destruction:
     * - Schedule disappear animation;
     * - Schedule view removal
     * @param view      Target view
     * @param container ViewGroup that contains the view
     */
    private fun scheduleDestruction(view : View, container : ViewGroup) {
        Handler(Looper.getMainLooper()).postDelayed({ container.removeView(view) }, dataHolder.duration - CooldownDuration)
        Handler(Looper.getMainLooper()).postDelayed({ fade(view, false) }, dataHolder.duration - AnimationDuration - CooldownDuration)
    }

    private fun buildLayoutParams() : LayoutParams {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = mGravity
        when (mGravity) {
            Gravity.BOTTOM -> layoutParams.setMargins(0, 0,0, VerticalMargin)
            Gravity.TOP -> layoutParams.setMargins(0, VerticalMargin, 0, 0)
        }
        return layoutParams
    }

    private fun fade(view : View, fadeIn : Boolean) {
        val animation = getAnimation(fadeIn)
        animation.duration = AnimationDuration.toLong()
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    private fun getAnimation(fadeIn : Boolean) = AlphaAnimation(if (fadeIn) InactiveToastAlpha else ActiveToastAlpha, if (fadeIn) ActiveToastAlpha else InactiveToastAlpha)

    /**
     * [View.OnClickListener] implementation
     * @param v View that was clicked
     */
    override fun onClick(v: View?) = copyToClipboard(activity, dataHolder)

}