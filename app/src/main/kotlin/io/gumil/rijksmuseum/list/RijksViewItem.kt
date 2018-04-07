package io.gumil.rijksmuseum.list

import android.annotation.SuppressLint
import android.view.View
import io.gumil.rijksmuseum.CollectionItem
import io.gumil.rijksmuseum.R
import io.gumil.rijksmuseum.common.ViewItem
import io.gumil.rijksmuseum.common.load
import kotlinx.android.synthetic.main.li_collection.view.*

internal class RijksViewItem : ViewItem<CollectionItem> {
    override var onItemClick: ((CollectionItem) -> Unit)? = null

    override val layout: Int = R.layout.li_collection

    @SuppressLint("CheckResult")
    override fun bind(view: View, item: CollectionItem) {
        super.bind(view, item)
        view.collectionImage.load(item.image)
        view.collectionTitle.text = item.title
    }
}