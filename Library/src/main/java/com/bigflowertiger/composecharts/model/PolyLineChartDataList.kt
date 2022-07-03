package com.bigflowertiger.composecharts.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

data class PolyLineChartDataList(
    val data: List<ChartData>
) {
    /**
     * 图表数据类
     * @param highestLinePoint 高折线图坐标
     * @param lowestLinePoint 低折线图坐标
     * @param title 每列第一行的主标题
     * @param titleColor 主标题文字颜色
     * @param titleFontWeight 主标题 FontWeight
     * @param subtitle 每列第二行副标题
     * @param topIcon 折线图上方的图标
     * @param bottomIcon 折线图下方的图标
     * @param topIconTitle 折线图上方的图标的标题
     * @param bottomIconTitle 折线图下方的图标的标题
     *
     */
    data class ChartData(
        val highLinePointsList: PolyLinePoints,
        val highestLinePoint: Float,
        val title: String,
        val titleColor: Color,
        val titleFontWeight: FontWeight,
        val subtitle: String,
        val topIcon: Int,
        val topIconTitle: String,
        val lowestLinePoint: Float,
        val lowLinePointsList: PolyLinePoints,
        val bottomIcon: Int? = null,
        val bottomIconTitle: String? = null,
        val histogramHighPoint: Float = 0f,
        val histogramTitle: String = "",
        val highestHistogramPoint: Float,
        val lowestHistogramPoint: Float
    )

    data class PolyLinePoints(val start: Float?, val mid: Float, val end: Float?)
}