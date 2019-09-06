package com.tlsolution.tlsmodules.Notice

import android.os.Bundle
import com.tlsolution.tlsmodules.Extensions.formattedString
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_notice_detail.*

class NoticeDetailActivity : TLSActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)

        setUpActionBar(findViewById(R.id.announcementDetailActionBar), "공지사항")
        intent.getParcelableExtra<Notice>(NoticeManager.NOTICES_DATA).let {
            displayData(it)
        }
    }

    private fun displayData(notice: Notice) {
        titleTextView.setText(notice.title)
        dateTextView.setText(notice.date.formattedString())
        contentTextView.setText(notice.content)
    }
}
