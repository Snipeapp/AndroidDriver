package ru.snipe.snipedriver.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
  val view = currentFocus
  if (view != null) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
  }
}

fun Activity.showKeyboard() {
  val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  keyboard.showSoftInput(currentFocus, 0)
}