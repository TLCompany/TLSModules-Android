package com.tlsolution.tlsmodules.Deprecated

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Extensions.formattedString
import com.tlsolution.tlsmodules.Models.Inquery
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.list_item_inquery.view.*

@Deprecated(message = "View-related classes are no longer supoported.")
class InqueryAdapter(): RecyclerView.Adapter<InqueryViewHolder>() {

    var inqueries = arrayListOf<Inquery>()

    override fun getItemCount(): Int {
        return inqueries.size
    }

    override fun onBindViewHolder(p0: InqueryViewHolder, p1: Int) {
        val itemView = p0.itemView
        val inquery = inqueries.get(p1)
        p0.inquery = inquery
        itemView.actionBarTitleTextView.setText(inquery.content)
        itemView.dateTextView.setText(inquery.date.formattedString())
        val inqueryText = if (inquery.isAnswered) "대답완료" else "답변 미완료"
        val inqueryColor = itemView.context.getColor(if (inquery.isAnswered) R.color.inquery_answered else R.color.inquery_unanswered)
        itemView.answerTextView.setText(inqueryText)
        itemView.answerTextView.setTextColor(inqueryColor)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InqueryViewHolder {
        val layoutInflator = LayoutInflater.from(p0.context)
        val itemView = layoutInflator.inflate(R.layout.list_item_inquery, p0, false)
        return InqueryViewHolder(itemView)
    }
}