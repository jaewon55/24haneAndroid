package com.hane24.hoursarenotenough24.utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hane24.hoursarenotenough24.R
import com.hane24.hoursarenotenough24.data.CalendarItem
import com.hane24.hoursarenotenough24.data.LogTableItem
import com.hane24.hoursarenotenough24.inoutlog.LogListFragment
import com.hane24.hoursarenotenough24.inoutlog.LogTableAdapter

fun getColorHelper(context: Context, id: Int) =
    if (Build.VERSION.SDK_INT >= 23) context.getColor(id) else context.resources.getColor(id)

@BindingAdapter("latestTime", "isStateOn", requireAll = false)
fun latestTimeText(
    view: TextView,
    timeString: String,
    state: Boolean
) {
    if (state) {
        view.text = view.context.getString(R.string.latest_time_format, timeString)
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}

@BindingAdapter(value = ["isStateOn"], requireAll = false)
fun ProgressBar.isStateOn(state: Boolean) {
    if (state) {
        progressTintList =
            ColorStateList.valueOf(getColorHelper(context, R.color.on_progress_front))
        progressBackgroundTintList =
            ColorStateList.valueOf(getColorHelper(context, R.color.on_progress_back))
    } else {
        progressTintList =
            ColorStateList.valueOf(getColorHelper(context, R.color.off_progress_front))
        progressBackgroundTintList =
            ColorStateList.valueOf(getColorHelper(context, R.color.off_progress_back))
    }
}

@BindingAdapter("logoColor")
fun bindLogoColor(
    view: ImageView,
    state: Boolean
) {
    view.imageTintList = if (state) {
        ColorStateList.valueOf(getColorHelper(view.context, R.color.on_icon_color))
    } else {
        ColorStateList.valueOf(getColorHelper(view.context, R.color.off_icon_color))
    }
}

@BindingAdapter("isEnabled")
fun bindRefreshClickable(
    view: ImageView,
    state: Boolean
) {
    view.isEnabled = state
}

@BindingAdapter("overViewLoading", "listViewLoading")
fun bindRefreshVisible(
    view: ImageView,
    overViewState: Boolean,
    listViewState: Boolean
) {
    view.visibility = if (overViewState || listViewState) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("overViewLoading", "listViewLoading")
fun bindProgressVisible(
    view: ProgressBar,
    overViewState: Boolean,
    listViewState: Boolean
) {
    view.visibility = if (overViewState || listViewState) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("isEnabled")
fun bindDrawerClickable(
    view: ImageButton,
    state: Boolean
) {
    view.isEnabled = state
}


@BindingAdapter("buttonState")
fun bindLeftButtonState(
    button: ImageButton,
    state: Boolean
) {
    button.isEnabled = state
}

@BindingAdapter("tableList")
fun bindTableRecyclerView(
    recyclerView: RecyclerView,
    data: List<LogTableItem>?
) {
    recyclerView.visibility = if (data?.isEmpty() != false) View.INVISIBLE else View.VISIBLE
    val adapter = recyclerView.adapter as LogTableAdapter
    adapter.submitList(data?.reversed())
}

@BindingAdapter("tableList")
fun bindTableTextView(
    textView: TextView,
    data: List<LogTableItem>?
) {
    textView.visibility = if (data?.isEmpty() != false) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("calendarGrid")
fun bindCalendarRecyclerView(
    recyclerView: RecyclerView,
    data: List<CalendarItem>?
) {
    val adapter = recyclerView.adapter as LogListFragment.LogCalendarAdapter
    adapter.submitList(data)
}

@BindingAdapter("item", "selectedDay", requireAll = false)
fun bindCalendarItem(
    view: MaterialButton,
    item: CalendarItem,
    selectedDay: Int
) {
    view.text = item.day.toString()
    view.backgroundTintList = ColorStateList.valueOf(getColorHelper(view.context, item.color))
    view.strokeWidth = 0
    if (item.day == selectedDay) {
        view.setTextColor(getColorHelper(view.context, R.color.selected_color))
    } else {
        view.setTextColor(getColorHelper(view.context, R.color.black))
//        ColorStateList.valueOf(getColorHelper(view.context, R.color.calendar_item_stroke_default))
    }
    if (item.isNextDay) {
        view.setTextColor(getColorHelper(view.context, R.color.next_day_text))
        view.isEnabled = false
    } else {
//        view.setTextColor(getColorHelper(view.context, R.color.black))
        view.isEnabled = true
    }
}

@BindingAdapter("loadingState")
fun setCalendarVisible(
    recyclerView: RecyclerView,
    loadingState: Boolean
) {
    recyclerView.visibility = if (loadingState) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("loadingState")
fun setProgressBarVisible(
    progressBar: ProgressBar,
    loadingState: Boolean
) {
    progressBar.visibility = if (loadingState) View.VISIBLE else View.INVISIBLE
}

