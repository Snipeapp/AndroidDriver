package ru.snipe.snipedriver.ui.views;

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class SimpleClickableSpan(private val onTextClickAction: (Boolean) -> Unit) : ClickableSpan() {
  override fun onClick(widget: View?) {
    onTextClickAction.invoke(true)
  }

  override fun updateDrawState(ds: TextPaint?) {
    super.updateDrawState(ds)
    ds?.isUnderlineText = false
    ds?.isFakeBoldText = false
  }
}