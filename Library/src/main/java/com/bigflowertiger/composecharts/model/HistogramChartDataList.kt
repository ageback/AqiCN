package com.bigflowertiger.composecharts.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

data class HistogramChartDataList(val data: List<HistogramChartData>) {

    data class HistogramChartData(
        val high: Float,
        val low: Float,
        val highest: Float,
        val lowest: Float,
        val title: String,
        val titleColor: Color,
        val titleFontWeight: FontWeight,
        val titleFontSize: TextUnit = TextUnit.Unspecified,
        val subtitle: String,
        val highTitle: String,
        val lowTitle: String,
        val histogramColor: List<Color>,
        val iconId: Int = -1
    )
}

