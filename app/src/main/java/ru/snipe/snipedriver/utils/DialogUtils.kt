package ru.snipe.snipedriver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.Px
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.PopupMenu
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import ru.snipe.snipedriver.R

//TODO: Исправить подтягивание стилей из темы для диалога
fun Context?.createBottomSheetThemed(): BottomSheetDialog {
  val wrapper = ContextThemeWrapper(this, R.style.AppTheme)
  return BottomSheetDialog(wrapper)
}

@SuppressLint("RestrictedApi")
fun <T> Context.createListWindow(titleItems: List<T>,
                                 anchorView: View,
                                 onWindowItemClickAction: (view: View, titleItem: T, itemPosition: Int) -> Unit,
                                 @Px widthPx: Int = this.dpToPx(POP_UP_WINDOWS_WIDTH_DP),
                                 @LayoutRes itemLayoutRes: Int = R.layout.item_spinner,
                                 gravity: Int = Gravity.END): ListPopupWindow {
  val itemsAdapter = ArrayAdapter<T>(this, itemLayoutRes).apply { addAll(titleItems) }

  val window = ListPopupWindow(this)
  window.setAdapter(itemsAdapter)
  window.setOnItemClickListener { adapterView, view, position, l ->
    window.dismiss()
    onWindowItemClickAction(adapterView, titleItems[position], position)
  }
  window.width = widthPx
  window.anchorView = anchorView
  window.setDropDownGravity(gravity)
  window.setOverlapAnchor(true)
  window.setBackgroundDrawable(getDrawableCompat(R.drawable.bg_popup))
  return window.withHorizontalOffset(-POP_UP_WINDOWS_HORIZONTAL_OFFSET_PX)
}

fun <T> Context.createPopupMenu(titleItems: List<T>,
                                anchorView: View,
                                onItemClickAction: (view: View?, titleItem: T, itemPosition: Int) -> Unit): PopupMenu {
  val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)
  return PopupMenu(wrapper, anchorView).apply {
    titleItems.forEachIndexed { index, title ->
      menu.add(Menu.NONE, index, index, title.toString())
    }
    setOnMenuItemClickListener { menuItem ->
      onItemClickAction.invoke(null, titleItems[menuItem.itemId], menuItem.itemId)
      true
    }
  }
}

fun ListPopupWindow.withVerticalOffset(@Px verticalOffsetPx: Int = this.anchorView!!.height): ListPopupWindow {
  this.verticalOffset = verticalOffsetPx
  return this
}

fun ListPopupWindow.withHorizontalOffset(@Px horizontalOffsetPx: Int): ListPopupWindow {
  this.width -= (horizontalOffsetPx * 2)
  this.horizontalOffset = horizontalOffsetPx
  return this
}