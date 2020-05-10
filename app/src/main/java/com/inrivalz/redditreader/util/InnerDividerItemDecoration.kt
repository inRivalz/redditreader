package com.inrivalz.redditreader.util

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.inrivalz.redditreader.util.InnerDividerItemDecoration.Companion.HORIZONTAL
import com.inrivalz.redditreader.util.InnerDividerItemDecoration.Companion.VERTICAL
import kotlin.math.roundToInt

/**
 * Port of the [androidx.recyclerview.widget.DividerItemDecoration] that allows for skipping the devider after the last item in the list.
 *
 * It is a [RecyclerView.ItemDecoration] that can be used as a divider
 * between items of a [LinearLayoutManager]. It supports both [HORIZONTAL] and [VERTICAL] orientations.
 */
class InnerDividerItemDecoration
/**
 * Creates a divider [RecyclerView.ItemDecoration] that can be used with a [LinearLayoutManager].
 *
 * @param orientation Divider orientation. Should be [HORIZONTAL] or [VERTICAL].
 * @param divider Drawable to use as divider.
 * @param skipLastItem Flag that indicates if we want to skip drawing the divider after the last item. By default it is true, so the last divider
 * will not be drawn unless explicitly indicated, supporting our most common use case.
 */
    (var orientation: Int, private val divider: Drawable, private val skipLastItem: Boolean = true) : RecyclerView.ItemDecoration() {

    /**
     * Current orientation. Either [HORIZONTAL] or [VERTICAL].
     */
    private val boundsRect = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        if (orientation == VERTICAL) {
            drawVertical(canvas, parent)
        } else {
            drawHorizontal(canvas, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, recyclerView: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (recyclerView.clipToPadding) {
            left = recyclerView.paddingLeft
            right = recyclerView.width - recyclerView.paddingRight
            canvas.clipRect(left, recyclerView.paddingTop, right, recyclerView.height - recyclerView.paddingBottom)
        } else {
            left = 0
            right = recyclerView.width
        }

        drawVerticalDividers(canvas, recyclerView, getDividersCount(recyclerView), left, right)
        canvas.restore()
    }

    private fun drawVerticalDividers(canvas: Canvas, recyclerView: RecyclerView, dividersCount: Int, left: Int, right: Int) {
        for (i in 0 until dividersCount) {
            val child = recyclerView.getChildAt(i)
            recyclerView.getDecoratedBoundsWithMargins(child, boundsRect)
            val bottom = boundsRect.bottom + child.translationY.roundToInt()
            val top = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, recyclerView: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (recyclerView.clipToPadding) {
            top = recyclerView.paddingTop
            bottom = recyclerView.height - recyclerView.paddingBottom
            canvas.clipRect(recyclerView.paddingLeft, top, recyclerView.width - recyclerView.paddingRight, bottom)
        } else {
            top = 0
            bottom = recyclerView.height
        }

        drawHorizontalDividers(canvas, recyclerView, getDividersCount(recyclerView), top, bottom)
        canvas.restore()
    }

    private fun drawHorizontalDividers(canvas: Canvas, recyclerView: RecyclerView, dividersCount: Int, top: Int, bottom: Int) {
        for (i in 0 until dividersCount) {
            val child = recyclerView.getChildAt(i)
            recyclerView.layoutManager!!.getDecoratedBoundsWithMargins(child, boundsRect)
            val right = boundsRect.right + child.translationX.roundToInt()
            val left = right - divider.intrinsicWidth
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL) {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    private fun getDividersCount(recyclerView: RecyclerView): Int =
        if (skipLastItem) {
            recyclerView.childCount - 1
        } else {
            recyclerView.childCount
        }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
    }
}
