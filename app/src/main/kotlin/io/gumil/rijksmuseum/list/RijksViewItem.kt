package io.gumil.rijksmuseum.list

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.ViewItem
import io.gumil.rijksmuseum.common.load

internal class RijksViewItem : ViewItem<CollectionItem> {
    override var onItemClick: ((CollectionItem) -> Unit)? = null

    override val layout: Int = R.layout.li_collection

    @SuppressLint("CheckResult")
    override fun bind(view: View, item: CollectionItem) {
        super.bind(view, item)
        (view as? ImageView)?.load(item.image)
    }
}