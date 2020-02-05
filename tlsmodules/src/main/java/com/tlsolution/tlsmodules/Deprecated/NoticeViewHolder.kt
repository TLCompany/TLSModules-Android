package com.tlsolution.tlsmodules.Deprecated

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Models.Notice

@Deprecated(message = "View-related classes are no longer supoported.")
class NoticeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var notice: Notice? = null

    init {
        itemView.setOnClickListener {
            check(notice != null) {
                return@setOnClickListener
            }

            val intent = Intent(itemView.context, NoticeDetailActivity::class.java)
            intent.putExtra(NoticeManager.NOTICES_DATA, notice)
            itemView.context.startActivity(intent)
        }
    }

}