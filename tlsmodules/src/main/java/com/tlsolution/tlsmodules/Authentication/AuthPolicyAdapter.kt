package com.tlsolution.tlsmodules.Authentication

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Extensions.mandatoryCounts
import com.tlsolution.tlsmodules.Logger
import com.tlsolution.tlsmodules.Policy.Policy
import com.tlsolution.tlsmodules.Policy.PolicyDetailActivity
import com.tlsolution.tlsmodules.Policy.PolicyManager
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.list_item_policy_agreement.view.*


class AuthPolicyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var policy: Policy? = null
    var selectAction: (Boolean) -> Unit = { }
    var isSelected: Boolean
        get() {
            return this.itemView.agreeButton.isSelected
        }
        set(value) {
            check(policy != null && policy!!.isMandatory) {
                return
            }
            this.itemView.agreeButton.isSelected = value
        }

    init {

        itemView.agreeButton.setOnClickListener {
            it.isSelected = !it.isSelected
            check(policy != null && policy!!.isMandatory) {
                return@setOnClickListener
            }
            selectAction(it.isSelected)
        }

        itemView.titleTextView.setOnClickListener {
            val intent = Intent(itemView.context, PolicyDetailActivity::class.java)
            intent.putExtra(PolicyManager.POLICY_DATA, policy)
            itemView.context.startActivity(intent)
        }
    }
}

class Policy {

}

class AuthPolicyAdapter(): RecyclerView.Adapter<AuthPolicyViewHolder>() {

    var policies = arrayListOf<Policy>()
    var selectAction: (Boolean) -> Unit = {}

    var isAllSelected: Boolean = false
        set(value) {
            field = value
            count = if (value) policies.mandatoryCounts() else 0
            this.notifyDataSetChanged()
        }

    private var count: Int = 0

    override fun getItemCount(): Int {

        return policies.size
    }

    override fun onBindViewHolder(p0: AuthPolicyViewHolder, p1: Int) {
        val itemView = p0.itemView
        val policy = policies.get(p1)
        p0.policy = policy
        p0.isSelected = this.isAllSelected

        p0.selectAction = { isSelected ->
            count += if (isSelected) 1 else - 1
            Logger.showDebuggingMessage("count", "${isSelected.toString()}, ${count.toString()}")
            if (count ==  policies.mandatoryCounts()) {
                isAllSelected = true
            }
            selectAction(if (count ==  policies.mandatoryCounts()) true else false)
            Unit
        }

        itemView.titleTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        itemView.titleTextView.setText(policy.title)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AuthPolicyViewHolder {
        val layoutInflator = LayoutInflater.from(p0.context)
        val itemView = layoutInflator.inflate(R.layout.list_item_policy_agreement, p0, false)
        return AuthPolicyViewHolder(itemView)
    }
}
