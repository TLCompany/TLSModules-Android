package com.tlsolution.tlsmodules.Deprecated

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_policy_list.*

@Deprecated(message = "View-related classes are no longer supoported.")
class PolicyListActivity : TLSActivity() {

    private lateinit var policyAdapter: PolicyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_list)

        setUpActionBar(findViewById(R.id.policyListActionBar), "정책사항")
        val policies = intent.getParcelableArrayListExtra<Policy>(PolicyManager.POLICY_DATA)
        policyAdapter = PolicyAdapter()
        policyRecyclerView.adapter = policyAdapter
        policyRecyclerView.layoutManager = LinearLayoutManager(this)
        policyAdapter.policies = policies
        policyAdapter.notifyDataSetChanged()
    }
}
