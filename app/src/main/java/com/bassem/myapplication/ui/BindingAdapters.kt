package com.bassem.myapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bassem.myapplication.ui.main.MainAdapter
import com.bassem.myapplication.ui.main.MainViewModel
import com.bassem.myapplication.model.AsteroidModel
import com.bassem.myapplication.model.PictureOfDay
import com.bassem.myapplication.ui.main.AsteroidApiStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.R

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = (imageView.context.getString(R.string.potentially_hazardous_asteroid_image))
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = (imageView.context.getString(R.string.not_hazardous_asteroid_image))
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = (imageView.context.getString(R.string.potentially_hazardous_asteroid_image))
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = (imageView.context.getString(R.string.not_hazardous_asteroid_image))
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.astronomical_unit_format), number)

}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AsteroidModel>?) {
    val adapter = recyclerView.adapter as MainAdapter
    adapter.submitList(data)
}

@BindingAdapter("asteroidApiStatus")
fun bindStatus(statusImageView: View, status: AsteroidApiStatus) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
        }
        AsteroidApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
        }
        AsteroidApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (pictureOfDay.mediaType.equals("image", true)) {
            val imgUri = pictureOfDay.url.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(imgView)
        } else {
            imgView.setImageResource(R.drawable.ic_broken_image)
        }
    }
}
