package com.itechevo.waraclecakes.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.itechevo.waraclecakes.R

const val CROSS_FADE_DELAY = 100

fun ImageView.loadRoundedImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_place_holder_image)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DELAY))
        .into(this)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_place_holder_image)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DELAY))
        .into(this)
}