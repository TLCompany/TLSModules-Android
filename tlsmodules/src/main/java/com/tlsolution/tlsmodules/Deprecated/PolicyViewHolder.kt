package com.tlsolution.tlsmodules.Deprecated

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Models.Policy

@Deprecated(message = "View-related classes are no longer supoported.")
class PolicyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var policy: Policy? = null

    init {
        itemView.setOnClickListener {
            check(policy != null) {
                return@setOnClickListener
            }

            val intent = Intent(itemView.context, PolicyDetailActivity::class.java)
            intent.putExtra(PolicyManager.POLICY_DATA, policy)
            itemView.context.startActivity(intent)
        }
    }
}