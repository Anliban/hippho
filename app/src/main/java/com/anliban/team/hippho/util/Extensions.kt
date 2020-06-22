package com.anliban.team.hippho.util

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.anliban.team.hippho.widget.OnSnapPositionChangeListener
import com.anliban.team.hippho.widget.SnapOnScrollListener
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

inline fun <T : View> T.afterMeasure(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun <T : Any> SavedStateHandle.requireValue(key: String): T = requireNotNull(get<T>(key))

fun dp2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun Double.roundTo2Decimal() = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

fun Fragment.showSnackBar(message: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}
