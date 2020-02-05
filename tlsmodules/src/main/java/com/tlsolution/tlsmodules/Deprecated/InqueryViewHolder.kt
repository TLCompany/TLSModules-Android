package com.tlsolution.tlsmodules.Deprecated

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Models.Inquery

@Deprecated(message = "View-related classes are no longer supoported.")
class InqueryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var inquery: Inquery? = null

    init {
        itemView.setOnClickListener {
            check(inquery != null) {
                return@setOnClickListener
            }

            val intent = Intent(itemView.context, InqueryDetailActivity::class.java)
            intent.putExtra(InqueryManager.INQUERY_DATA, inquery)
            itemView.context.startActivity(intent)
        }
    }
}