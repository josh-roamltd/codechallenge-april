package joshvdh.com.codechallengeapril.component

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.RelativeLayout

abstract class BaseViewHolder(context: Context) : RecyclerView.ViewHolder(RelativeLayout(context)) {
    init {
        onCreateView(itemView as RelativeLayout)
    }

    protected abstract fun onCreateView(root: RelativeLayout)
}