package com.tlsolution.tlsmodules.Notice


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tlsolution.tlsmodules.Extensions.formattedString
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.list_item_title_with_date.view.*

class NoticeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var notice: Notice? = null

    init {
        itemView.setOnClickListener {
            check(notice != null) {
                return@setOnClickListener
            }

            val intent = Intent(itemView.context, NoticeDetailActivity::class.java)
            intent.putExtra(NoticeManager.NOTICES_DATA, notice)
            itemView.context.startActivity(intent)
        }
    }

}

class NoticeAdapter(): RecyclerView.Adapter<NoticeViewHolder>() {

    var notices = arrayListOf<Notice>()

    override fun getItemCount(): Int {
        return notices.size
    }

    override fun onBindViewHolder(p0: NoticeViewHolder, p1: Int) {
        val itemView = p0.itemView
        val notice = notices.get(p1)
        itemView.dateTextView.setText(notice.date.formattedString())
        itemView.titleTextView.setText(notice.title)
        p0.notice = notice
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoticeViewHolder {
        val layoutInflator = LayoutInflater.from(p0.context)
        val itemView = layoutInflator.inflate(R.layout.list_item_title_with_date, p0, false)
        return NoticeViewHolder(itemView)
    }

}