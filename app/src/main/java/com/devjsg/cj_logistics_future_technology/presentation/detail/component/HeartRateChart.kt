package com.devjsg.cj_logistics_future_technology.presentation.detail.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.devjsg.cj_logistics_future_technology.presentation.detail.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun HeartRateChart(heartRateData: List<HeartRateData>) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                axisLeft.isEnabled = false
                axisRight.isEnabled = true
                axisLeft.axisMinimum = 0f

                axisRight.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)

                setScaleEnabled(false)
                setPinchZoom(false)

                setTouchEnabled(false)

                legend.isEnabled = false
                setVisibleXRangeMaximum(7f)
                moveViewToX(0f)

                xAxis.textSize = 12f
                axisRight.textSize = 12f

                renderer = RoundedBarChartRenderer(this, animator, viewPortHandler)
            }
        },
        update = { chart ->
            val entries = heartRateData.mapIndexed { index, data ->
                BarEntry(
                    index.toFloat(),
                    data.maxHeartRate.toFloat()
                )
            }

            val barDataSet = BarDataSet(entries, "").apply {
                color = ContextCompat.getColor(chart.context, R.color.cj_red)
                setDrawValues(false)
            }

            val data = BarData(barDataSet)
            chart.data = data

            chart.barData.barWidth = 0.2f

            val xAxis = chart.xAxis
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return heartRateData.getOrNull(value.toInt())?.dateTime?.substring(11, 16)
                        ?: value.toString()
                }
            }

            chart.invalidate()
        }
    )
}