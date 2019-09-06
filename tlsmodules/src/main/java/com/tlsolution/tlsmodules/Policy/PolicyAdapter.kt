package com.tlsolution.tlsmodules.Policy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.activity_notice_detail.view.titleTextView

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