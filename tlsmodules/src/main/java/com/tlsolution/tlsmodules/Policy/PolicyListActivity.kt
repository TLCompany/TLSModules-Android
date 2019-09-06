package com.tlsolution.tlsmodules.Policy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_policy_list.*

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
