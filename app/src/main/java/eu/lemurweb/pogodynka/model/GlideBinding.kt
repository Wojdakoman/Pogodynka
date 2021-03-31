package eu.lemurweb.pogodynka.model

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}