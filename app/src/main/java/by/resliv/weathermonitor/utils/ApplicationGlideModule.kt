package by.resliv.weathermonitor.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * @author EpicDima
 */
@GlideModule
class ApplicationGlideModule : AppGlideModule() {
    companion object {
        @JvmStatic
        @BindingAdapter("iconUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view).load("https:$url").into(view)
            }
        }
    }
}
