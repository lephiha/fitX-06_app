package com.lephiha.fitx_06.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lephiha.fitx_06.Container.Activity
import com.lephiha.fitx_06.R

class ScheduleAdapter(
    private var todayList: List<Activity>,
    private var upcomingList: List<Activity>,
    private val onCheckInClick: (Activity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TODAY_HEADER = 0
        private const val TYPE_TODAY_ITEM = 1
        private const val TYPE_UPCOMING_HEADER = 2
        private const val TYPE_UPCOMING_ITEM = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_TODAY_HEADER
            position <= todayList.size -> TYPE_TODAY_ITEM
            position == todayList.size + 1 -> TYPE_UPCOMING_HEADER
            else -> TYPE_UPCOMING_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TODAY_HEADER, TYPE_UPCOMING_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_TODAY_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule_today, parent, false)
                TodayViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule_upcoming, parent, false)
                UpcomingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                if (position == 0) {
                    holder.bind("Hôm nay")
                } else {
                    holder.bind("Lớp Group sắp tới")
                }
            }
            is TodayViewHolder -> {
                val activity = todayList[position - 1]
                holder.bind(activity, onCheckInClick)
            }
            is UpcomingViewHolder -> {
                val activity = upcomingList[position - todayList.size - 2]
                holder.bind(activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (todayList.isEmpty() && upcomingList.isEmpty()) {
            0
        } else {
            2 + todayList.size + upcomingList.size // 2 headers + items
        }
    }

    fun updateData(today: List<Activity>, upcoming: List<Activity>) {
        this.todayList = today
        this.upcomingList = upcoming
        notifyDataSetChanged()
    }

    // Header ViewHolder
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvHeader: TextView = view.findViewById(R.id.tvHeader)

        fun bind(title: String) {
            tvHeader.text = title
        }
    }

    // Today Item ViewHolder
    class TodayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTime: TextView = view.findViewById(R.id.tvTime)
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvCoach: TextView = view.findViewById(R.id.tvCoach)
        private val btnCheckIn: TextView = view.findViewById(R.id.btnCheckIn)
        private val llContainer: LinearLayout = view.findViewById(R.id.llContainer)

        fun bind(activity: Activity, onCheckInClick: (Activity) -> Unit) {
            // Format time: "18:00 - 19:00"
            val startTime = activity.startTime.substring(0, 5) // Remove seconds
            val endTime = activity.endTime.substring(0, 5)
            tvTime.text = "$startTime - $endTime"

            tvTitle.text = activity.title

            // Coach name from PT id - TODO: Get from API
            tvCoach.text = if (activity.ptId != null) "Coach #${activity.ptId}" else "Lớp Group"

            // Check-in button
            if (activity.isCheckedIn) {
                btnCheckIn.text = "Đã check-in"
                btnCheckIn.isEnabled = false
                btnCheckIn.alpha = 0.6f
            } else {
                btnCheckIn.text = "Check-in"
                btnCheckIn.isEnabled = true
                btnCheckIn.alpha = 1f
                btnCheckIn.setOnClickListener {
                    onCheckInClick(activity)
                }
            }

            // Background color based on type
            if (activity.type == "PT") {
                llContainer.setBackgroundResource(R.drawable.bg_gradient_blue)
            } else {
                llContainer.setBackgroundResource(R.drawable.bg_gradient_purple)
            }
        }
    }

    // Upcoming Item ViewHolder
    class UpcomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        private val tvSlots: TextView = view.findViewById(R.id.tvSlots)
        private val tvStatus: TextView = view.findViewById(R.id.tvStatus)

        fun bind(activity: Activity) {
            tvTitle.text = activity.title

            // Format date: "T6, 19:00"
            val dayOfWeek = getDayOfWeek(activity.date)
            val time = activity.startTime.substring(0, 5)
            tvDateTime.text = "$dayOfWeek, $time"

            // Slots - TODO: Get from API
            tvSlots.text = "0/20"
            tvStatus.text = "Chờ trống"
        }

        private fun getDayOfWeek(dateString: String): String {
            // Parse date string and get day of week
            // TODO: Implement proper date parsing
            return "T6"
        }
    }
}