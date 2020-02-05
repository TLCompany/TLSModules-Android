package com.tlsolution.tlsmodules.Deprecated

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.activity_notice_detail.view.titleTextView

@Deprecated(message = "View-related classes are no longer supoported.")
class PolicyAdapter(): RecyclerView.Adapter<PolicyViewHolder>() {

    var policies = arrayListOf<Policy>()

    override fun getItemCount(): Int {
        return policies.size
    }

    override fun onBindViewHolder(p0: PolicyViewHolder, p1: Int) {
        val itemView = p0.itemView
        val policy = policies.get(p1)
        p0.policy = policy
        itemView.titleTextView.setText(policy.title)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PolicyViewHolder {
        val layoutInflator = LayoutInflater.from(p0.context)
        val itemView = layoutInflator.inflate(R.layout.list_item_with_right_arrow, p0, false)
        return PolicyViewHolder(itemView)
    }
}