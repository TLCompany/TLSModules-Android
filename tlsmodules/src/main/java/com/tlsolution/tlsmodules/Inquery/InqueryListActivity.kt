package com.tlsolution.tlsmodules.Inquery

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_inquery_list.*

class InqueryListActivity : TLSActivity() {

    companion object {
        val TAG = "InqueryListActivity"
        val REQUEST_CODE = 11
    }

    lateinit var inqueryAdapter: InqueryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquery_list)

        title = "문의사항"
        setUpActionBar(findViewById(R.id.inqueryListActionBar), "문의사항", "문의하기")
        val inqueries = intent.getParcelableArrayListExtra<Inquery>(InqueryManager.INQUERY_DATA)
        inqueryAdapter = InqueryAdapter()
        inqueryRecyclerView.adapter = inqueryAdapter
        inqueryRecyclerView.layoutManager = LinearLayoutManager(this)
        inqueryAdapter.inqueries = inqueries
        inqueryAdapter.notifyDataSetChanged()
    }

    override fun rightAction() {
        val intent = Intent(this, NewInqueryActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //새로운 문의사항 -> 서버 -> 클라이언트
        if (requestCode == REQUEST_CODE && data != null) {
            val inequeries = data.getParcelableArrayListExtra<Inquery>(NewInqueryActivity.COMPLETED)
            inqueryAdapter.inqueries = inequeries
            inqueryAdapter.notifyDataSetChanged()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
