package com.akhbulatov.wordkeeper.presentation.ui.global.views

import android.content.Context
import android.util.AttributeSet
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Provides support to the context menu for RecyclerView
 * Source: https://gist.github.com/FrancoisBlavoet/cba0aa150e60b727636d
 */
class ContextMenuRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mContextMenuInfo: ContextMenuInfo? = null

    override fun getContextMenuInfo(): ContextMenuInfo? {
        return mContextMenuInfo
    }

    override fun showContextMenuForChild(originalView: View): Boolean {
        val longPressPosition = getChildLayoutPosition(originalView)
        if (longPressPosition >= 0) {
            val longPressId = adapter!!.getItemId(longPressPosition)
            mContextMenuInfo = createContextMenuInfo(longPressPosition, longPressId)
            return super.showContextMenuForChild(originalView)
        }
        return false
    }

    private fun createContextMenuInfo(position: Int, id: Long): ContextMenuInfo {
        return RecyclerContextMenuInfo(position, id)
    }

    /**
     * Extra menu information provided to the
     * {@link android.view.View.OnCreateContextMenuListener#onCreateContextMenu(android.view.ContextMenu, View, ContextMenuInfo) }
     * callback when a context menu is brought up for this AdapterView.
     */
    class RecyclerContextMenuInfo(

        /**
         * The position in the adapter for which the context menu is being
         * displayed.
         */
        val position: Int,

        /**
         * The row id of the item for which the context menu is being displayed.
         */
        val id: Long
    ) : ContextMenuInfo
}