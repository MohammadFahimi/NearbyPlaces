package com.mfahimi.nearbyplace.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mfahimi.nearbyplace.R
import com.mfahimi.nearbyplace.data.network.exceptions.NetworkException
import com.mfahimi.nearbyplace.ext.gone
import com.mfahimi.nearbyplace.ext.visible
import kotlinx.coroutines.flow.Flow


class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnClickListener {

    enum class Background { TRANSPARENT, WHITE, GREY }

    private val view: View
    private val root: ConstraintLayout
    private val messageTv: TextView
    private val image: ImageView
    private val loading: ProgressBar
    private val retry: LoadingButton

    private var noView = false
    private var background = Background.TRANSPARENT
    private var enableBackground = true
    private var listener: OnClickListener? = null
    private var tag: Int = 0// show current requested service

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingView,
            0, 0
        )
        typedArray.apply {
            if (hasValue(R.styleable.LoadingView_lv_enableBackground))
                enableBackground = getBoolean(R.styleable.LoadingView_lv_enableBackground, true)
            if (hasValue(R.styleable.LoadingView_lv_noView))
                noView = getBoolean(R.styleable.LoadingView_lv_noView, false)
            if (hasValue(R.styleable.LoadingView_backgroundValue)) {
                background = Background.values()[getInt(R.styleable.LoadingView_backgroundValue, 0)]
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        view = inflater.inflate(R.layout.view_loading, null)
        root = view.findViewById(R.id.cl_root)
        messageTv = view.findViewById(R.id.tv_error_message)
        image = view.findViewById(R.id.img_error)
        loading = view.findViewById(R.id.lottie_view)
        retry = view.findViewById(R.id.btn_retry)
        retry.setOnClickListener(this)
        setBackground(background)

        if (noView)
            hideAll()
        if (enableBackground)
            enableBackground()
        else
            disableBackground()
        addView(view)
    }

    private fun hideAll() {
        messageTv.gone()
        image.gone()
        loading.gone()
        retry.gone()
    }

    fun setErrorMessage(message: String?, @DrawableRes drawableId: Int? = null) {
        retry.hideLoading()
        messageTv.text = message
        messageTv.visible()
        retry.visible()
        loading.gone()
        if (drawableId != null) {
            setImage(drawableId)
            image.visible()
        } else
            setImage(R.drawable.ic_default_error)
        root.setBackgroundColor(ContextCompat.getColor(context, R.color.windowBackground))
        disableBackground()
    }

    fun setErrorMessage(@StringRes stringId: Int, @DrawableRes drawableId: Int? = null) =
        setErrorMessage(context.getString(stringId), drawableId)

    fun <T> showLoading(value: Flow<T>, background: Background? = null) {
        if (!alreadyShowsLoading(value)) {
            showLoading()
        }
    }

    fun showLoading() {
        loading.visible()
        messageTv.gone()
        image.gone()
        retry.gone()
        if (background != null)
            setBackground(background)
        showView()
        messageTv.text = ""
        image.setImageResource(0)
        view.isClickable = true
    }

    fun resetLoadingTag() {
        tag = "this".hashCode()
    }

    private fun <T> alreadyShowsLoading(value: Flow<T>): Boolean {
        val res = tag == value.hashCode()
        tag = value.hashCode()
        return res
    }

    private fun setImage(@DrawableRes drawableId: Int) {
        image.setImageResource(drawableId)
    }

    fun hideView() {
        view.isClickable = false
        view.gone()
    }

    private fun showView() {
        view.visible()
    }

    fun showUnClickableView(background: Background? = null) {
        hideAll()
        showView()
        disableBackground()
        if (background != null)
            setBackground(background)
    }

    private fun disableBackground() {
        view.isClickable = true
    }

    private fun enableBackground() {
        view.isClickable = false
    }

    private fun setBackground(bg: Background) {
        this.background = bg
        root.setBackgroundColor(ContextCompat.getColor(context, getBackgroundColor()))
    }

    private fun getBackgroundColor(): Int = when (background) {
        Background.TRANSPARENT -> R.color.transparentBlack
        Background.WHITE -> R.color.windowBackground
        Background.GREY -> R.color.loadingGrey
    }

    override fun onClick(p0: View?) {
        if (listener != null && !retry.isLoading) {
            retry.showLoading()
            listener?.onClick(this)
        }

    }

    fun showFullScreenError(error: NetworkException, message: String?, retryAction: () -> Unit) {
        listener = OnClickListener { retryAction() }

        messageTv.text = getErrorMessage(error, message)
        when (error) {
            is NetworkException.DisconnectedError, is NetworkException.TimeoutError ->
                setImage(R.drawable.ic_no_internet)
            else ->
                setImage(R.drawable.ic_default_error)
        }
        image.visible()
        retry.hideLoading()
        messageTv.visible()
        retry.visible()
        loading.gone()
        root.setBackgroundColor(ContextCompat.getColor(context, R.color.windowBackground))
        disableBackground()
        view.visible()
    }

    fun handleConditionalApiError(
        error: NetworkException,
        message: String?,
        retryAction: () -> Unit
    ) {
        when (error) {
            is NetworkException.DisconnectedError, is NetworkException.TimeoutError ->
                showFullScreenError(error, message, retryAction)
            else -> {
                retry.hideLoading()
                hideView()
                showSnackError(getErrorMessage(error, message))
            }
        }
    }

    private fun showSnackError(message: String) {
        val snack = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
        snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snack.setTextColor(Color.WHITE)
        snack.view.background = ContextCompat.getDrawable(context, R.drawable.sh_snackbar_err_bg)
        snack.show()
    }

    fun showSnackSuccess(message: String) {
        val snack = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
        snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snack.setTextColor(Color.WHITE)
        snack.view.background =
            ContextCompat.getDrawable(context, R.drawable.sh_snackbar_success_bg)
        snack.show()
    }


    private fun getErrorMessage(error: NetworkException, msg: String?): String {
        if (msg != null)
            return msg
        return error.message ?: error::class.java.simpleName
    }


}