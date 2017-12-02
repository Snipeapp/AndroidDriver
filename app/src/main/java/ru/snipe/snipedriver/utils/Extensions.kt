package ru.snipe.snipedriver.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager

inline fun createIntent(ctx: Context, cls: Class<*>, action: Intent.() -> Unit): Intent
        = Intent(ctx, cls).apply(action)


inline fun <F : Fragment> createFragment(fragment: F, args: Bundle.() -> Unit): F {
    return fragment.apply {
        arguments = Bundle().apply(args)
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun Activity.showKeyboard() {
    val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboard.showSoftInput(currentFocus, 0)
}