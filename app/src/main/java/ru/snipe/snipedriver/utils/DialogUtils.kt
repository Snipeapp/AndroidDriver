package ru.snipe.snipedriver.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.Px
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import ru.snipe.snipedriver.R

fun <T> Context.createListWindow(titleItems: List<T>,
                                 anchorView: View,
                                 onWindowItemClickAction: (view: View, titleItem: T, itemPosition: Int) -> Unit,
                                 @Px widthPx: Int = this.dpToPx(POP_UP_WINDOWS_WIDTH_DP),
                                 @LayoutRes itemLayoutRes: Int = R.layout.item_spinner): ListPopupWindow {
  val itemsAdapter = ArrayAdapter<T>(this, itemLayoutRes).apply { addAll(titleItems) }

  val window = ListPopupWindow(this)
  window.setAdapter(itemsAdapter)
  window.setOnItemClickListener { adapterView, view, position, l ->
    window.dismiss()
    onWindowItemClickAction(adapterView, titleItems[position], position)
  }
  window.width = widthPx
  window.anchorView = anchorView
  return window
}

fun ListPopupWindow.withVerticalOffset(@Px verticalOffset: Int = this.anchorView.height): ListPopupWindow {
  this.verticalOffset = verticalOffset
  return this
}

fun ListPopupWindow.withHorizontalOffset(@Px horizontalOffset: Int = POP_UP_WINDOWS_HORIZONTAL_OFFSET_PX): ListPopupWindow {
  this.width -= (horizontalOffset * 2)
  this.horizontalOffset = horizontalOffset
  return this
}