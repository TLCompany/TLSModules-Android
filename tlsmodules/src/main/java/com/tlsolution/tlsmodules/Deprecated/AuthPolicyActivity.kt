package com.tlsolution.tlsmodules.Deprecated

import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlsolution.tlsmodules.Extensions.showNoActionConfirmAlert
import com.tlsolution.tlsmodules.Extensions.sortedOnes
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.activity_auth_policy.*

/**
 * 회원가입에서 필요한 정책사항의 동의를 구하는 Activity. intent로 정책사항(policy)를 주고 BroadcastReceiver로 결과를 받는다
 */
@Deprecated(message = "View-related classes are no longer supoported.")
class AuthPolicyActivity : AuthenticationActivity() {

    lateinit var adapter: AuthPolicyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_policy)

        setUpDescription(findViewById(R.id.authPolicyDescription), getString(R.string.auth_policy_description_title), getString(R.string.auth_policy_description_subtitle))
        setUpActionBar(authPolicyActionBar, "회원가입")
        setUpAdapter()
        agreeAllButton.setOnClickListener {
            it.isSelected = !it.isSelected
            adapter.isAllSelected = it.isSelected
        }

        nextButton.setOnClickListener {
            check(agreeAllButton.isSelected) {
                showNoActionConfirmAlert("", getString(R.string.auth_policy_warning))
                return@setOnClickListener
            }
            //동의가 다 됐다면, receiver에게 알려준다.
            val intent = Intent(AuthManager.POLICY_COMPLETED)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun setUpAdapter() {
        adapter = AuthPolicyAdapter()
        authPolicyRecyclerView.adapter = adapter
        authPolicyRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.policies = intent.getParcelableArrayListExtra<Policy>(AuthManager.POLICY_DATA).sortedOnes()
        adapter.notifyDataSetChanged()

        adapter.selectAction = { isAllSelected ->
            agreeAllButton.isSelected = isAllSelected
        }
    }
}
