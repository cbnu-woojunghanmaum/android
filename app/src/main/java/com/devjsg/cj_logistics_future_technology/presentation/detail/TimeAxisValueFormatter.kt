package com.devjsg.cj_logistics_future_technology.presentation.detail

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Date

class TimeAxisValueFormatter(private val heartRateData: List<HeartRateData>) : ValueFormatter() {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("HH:mm")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return if (heartRateData.isNotEmpty() && index >= 0 && index < heartRateData.size) {
            dateFormat.format(Date(heartRateData[index].dateTime))
        } else {
            Log.e("TimeAxisValueFormatter", "Invalid index: $index, value: $value, size: ${heartRateData.size}")
            ""
        }
    }
}