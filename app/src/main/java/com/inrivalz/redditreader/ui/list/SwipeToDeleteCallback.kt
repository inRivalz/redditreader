package com.inrivalz.redditreader.ui.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.inrivalz.redditreader.R

class SwipeToDeleteCallback(
    private val context: Context,
    private val onItemDeleted: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val icon: Drawable by lazy {
        context.resources.getDrawable(
            R.drawable.ic_delete, null
        )
    }
    private val margin = context.resources.getDimensionPixelSize(R.dimen.grid_32)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onItemDeleted(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        c.clipRect(0f, viewHolder.itemView.top.toFloat(), dX, viewHolder.itemView.bottom.toFloat())
        c.drawColor(Color.RED)

        val centerTop = viewHolder.itemView.top + viewHolder.itemView.height / 2
        icon.bounds = Rect(
            margin,
            centerTop - icon.intrinsicHeight / 2,
            margin + icon.intrinsicWidth,
            centerTop + icon.intrinsicHeight / 2
        )
        icon.draw(c)
    }
}
