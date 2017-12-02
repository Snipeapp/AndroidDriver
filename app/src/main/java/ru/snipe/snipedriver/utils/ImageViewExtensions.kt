package ru.snipe.snipedriver.utils

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.snipe.snipedriver.R

fun ImageView.loadCenterCropImage(imageUrl: String?,
                                  picasso: Picasso? = Picasso.with(context),
                                  placeholderConfig: PlaceholderConfig = PlaceholderConfig.default()) {
  if (!imageUrl.isNullOrBlank()) {
    (picasso ?: Picasso.with(context))
      .load(imageUrl)
      .fit()
      .centerCrop()
      .noFade()
      .apply {
        if (placeholderConfig.showPlaceholder && placeholderConfig.placeHolderRes != 0)
          placeholder(placeholderConfig.placeHolderRes)
      }
      .into(this)
  } else if (placeholderConfig.showPlaceholder && placeholderConfig.placeHolderRes != 0) {
    setImageResource(placeholderConfig.placeHolderRes)
  } else {
    setImageDrawable(null)
  }
}

data class PlaceholderConfig(val showPlaceholder: Boolean = false,
                             @DrawableRes val placeHolderRes: Int = 0) {
  companion object {
    fun default(showPlaceholder: Boolean = true) = PlaceholderConfig(showPlaceholder, R.drawable.bg_placeholder)
    fun none() = PlaceholderConfig(false, 0)
  }
}