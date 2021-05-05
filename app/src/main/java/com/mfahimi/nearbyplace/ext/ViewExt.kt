package com.mfahimi.nearbyplace.ext

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

fun View.setWindowInsetsListener(listener: (view: View, insets: WindowInsetsCompat) -> WindowInsetsCompat) {
    ViewCompat.setOnApplyWindowInsetsListener(this, listener)
}

fun View.requestWindowInsets(listener: (view: View, insets: WindowInsetsCompat) -> WindowInsetsCompat) {
    ViewCompat.setOnApplyWindowInsetsListener(this, listener)
    ViewCompat.requestApplyInsets(this)
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val lp = layoutParams
    if (lp is ViewGroup.MarginLayoutParams) {
        lp.topMargin = top
        lp.leftMargin = left
        lp.rightMargin = right
        lp.bottomMargin = bottom
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.color(@ColorRes id: Int) = ContextCompat.getColor(context, id)
fun Fragment.color(@ColorRes id: Int) = ContextCompat.getColor(context!!, id)
fun View.string(@StringRes id: Int): String = this.resources.getString(id)
fun View.string(@StringRes id: Int?): String = if (id == null) "" else this.resources.getString(id)
fun View.int(@IntegerRes id: Int): Int = context.resources.getInteger(id)
fun View.string(@StringRes id: Int, vararg formatArgs: Any): String = context.resources.getString(id, formatArgs)

fun View.dimen(@DimenRes id: Int) = context.resources.getDimension(id)
fun View.typeface(@FontRes id: Int) = ResourcesCompat.getFont(context, id)

fun View.setElevation21(elevation: Float) = ViewCompat.setElevation(this, elevation)

fun NestedScrollView.totalScroll() = getChildAt(0).measuredHeight - measuredHeight

fun ViewGroup.inflate(@LayoutRes res: Int, attach: Boolean): View = LayoutInflater.from(context).inflate(res, this, attach)


fun View.scaleAnimation(visible: Boolean, duration: Long) {

    if (visible && (visibility == View.VISIBLE || animation != null)) return
    if (!visible && (visibility != View.VISIBLE || animation != null)) return
    val src = if (visible) 0.0f else 1.0f
    val dest = 1.0f - src
    val anim = ScaleAnimation(
        src, dest, src, dest,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.duration = duration
    anim.fillAfter = true
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(anim: Animation?) {
            if (!visible) {
                visibility = View.INVISIBLE
            }
            animation = null
        }

        override fun onAnimationStart(anim: Animation?) {
            if (visible) {
                visibility = View.VISIBLE
            }
            animation = anim
        }
    })
    startAnimation(anim)
}

fun View.animateCollapse(duration: Long = 100) {
    scaleAnimation(false, duration)
}

fun View.animateExpand(duration: Long = 100) {
    scaleAnimation(true, duration)
}

fun DrawerLayout.lock() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}

fun DrawerLayout.unlock() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
}

fun View.getViewPosition(parent: View, activity: Activity): IntArray {
    val result = IntArray(2)
    val display = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(display)
//    val topOffset = display.heightPixels - parent.measuredHeight
    this.getLocationInWindow(result)
    return result
}