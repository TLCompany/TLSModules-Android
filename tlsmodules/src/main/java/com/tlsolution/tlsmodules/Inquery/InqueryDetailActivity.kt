package com.tlsolution.tlsmodules.Inquery

import android.os.Bundle
import android.view.View
import com.tlsolution.tlsmodules.Extensions.formattedString
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_inquery_detail_actvity.*

class InqueryDetailActivity : TLSActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquery_detail_actvity)

        setUpActionBar(findViewById(R.id.inqueryDetailActionBar), "나의문의")
        val inquery = intent.getParcelableExtra<Inquery>(InqueryManager.INQUERY_DATA)
        displayData(inquery)
    }

    private fun displayData(inquery: Inquery) {
        dateTextView.setText(inquery.date.formattedString())
        contentTextView.setText(inquery.content)
        if (inquery.isAnswered) {
            answerTextView.visibility = View.VISIBLE
            separator.visibility = View.VISIBLE
            answerTextView.setText(inquery.answer)
        } else {
            answerTextView.visibility = View.INVISIBLE
            separator.visibility = View.INVISIBLE
        }
    }
}
