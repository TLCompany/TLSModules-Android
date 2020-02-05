package com.tlsolution.tlsmodules.Deprecated

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlsolution.tlsmodules.Models.Notice
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_notice_list.*

@Deprecated(message = "View-related classes are no longer supoported.")
class NoticeListActivity : TLSActivity() {

    companion object {
        val TAG = "ListActivity"
    }

    lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)

        setUpActionBar(findViewById(R.id.noticeActionBar), "공지사항")
        val notices = intent.getParcelableArrayListExtra<Notice>(NoticeManager.NOTICES_DATA)
        noticeAdapter = NoticeAdapter()
        noticeListView.adapter = noticeAdapter
        noticeListView.layoutManager = LinearLayoutManager(this)
        noticeAdapter.notices = notices
        noticeAdapter.notifyDataSetChanged()
    }
}
