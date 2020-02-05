package com.tlsolution.tlsmodules.Deprecated

import android.os.Bundle
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_policy_detail.*

@Deprecated(message = "View-related classes are no longer supoported.")
class PolicyDetailActivity : TLSActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_detail)

        intent.getParcelableExtra<Policy>(PolicyManager.POLICY_DATA).let {
            displayData(it)
            setUpActionBar(findViewById(R.id.policyDetailActionBar), it.title)
        }
    }

    private fun displayData(policy: Policy) {
        title = policy.title
        contentTextView.setText(policy.content)
    }
}
