package com.example.pocketmanager.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketmanager.R
import com.example.pocketmanager.data.ReportItem

class ReportAdapter(
    var reportList: List<ReportItem>,
    private val context: Context
) : RecyclerView.Adapter<ReportAdapter.ViewHolder?>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var reportType: TextView
        var reportAmt: TextView
        var reportTime: TextView

        init {
            reportType = v.findViewById<View>(R.id.reportType) as TextView
            reportAmt = v.findViewById<View>(R.id.reportAmt) as TextView
            reportTime = v.findViewById<View>(R.id.reportTime) as TextView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.reportType.text = reportList[position].reportType
        holder.reportAmt.text = "Rs " + reportList[position].reportAmt
        holder.reportAmt.setTextColor(Color.RED)
        if (reportList[0].dayType == "Daily") holder.reportTime.text =
            reportList[position].reportTime else if (reportList[0].dayType == "Weekly") holder.reportTime.text =
            reportList[position].reportDayTime else if (reportList[0].dayType == "Monthly") holder.reportTime.text =
            reportList[position].reportDateTime
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

}