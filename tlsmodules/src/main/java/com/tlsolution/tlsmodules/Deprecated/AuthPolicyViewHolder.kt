package com.tlsolution.tlsmodules.Deprecated

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Models.Policy
import kotlinx.android.synthetic.main.list_item_policy_agreement.view.*

@Deprecated(message = "View-related classes are no longer supoported.")
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
