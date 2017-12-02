package ru.snipe.snipedriver.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

inline fun createIntent(ctx: Context, cls: Class<*>, action: Intent.() -> Unit): Intent
        = Intent(ctx, cls).apply(action)


inline fun <F : Fragment> createFragment(fragment: F, args: Bundle.() -> Unit): F {
    return fragment.apply {
        arguments = Bundle().apply(args)
    }
}