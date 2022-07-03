package com.bigflowertiger.composecharts

import android.graphics.Paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigflowertiger.composecharts.model.PolyLineChartDataList
import kotlin.math.roundToInt

fun DrawScope.drawCharts(
    polyLinePoints: PolyLineChartDataList.PolyLinePoints,
    highestLinePoint: Float,
    lowestLinePoint: Float,
    isHighLine: Boolean = true,
    lineColor: Color,
    lineTextColor: Color,
    histogramPoint: Float = 0f,
    highestHistogramPoint: Float = 0f,
    gridWidth: Int = dateBoxWidthDp,
    lowestHistogramPoint: Float = 0f
) {
    //圆点的集合
    val points: ArrayList<Offset> = ArrayList()

    val diff = highestLinePoint - lowestLinePoint


    //绘制的直线的宽度
    val lineStrokeWidth = 10f

    //绘制的最大圆点的直径，注意是半径，绘制时候需要乘以2
    val pointStrokeWidth = 12f

    val histogramWidthSp = 8

    val path = Path()


    val x0 = 0f
    val xMid = (pointStrokeWidth + gridWidth.dp.roundToPx()) / 2
    val xEnd = size.width

    val yMid = getY(polyLinePoints.mid, diff, lowestLinePoint)

    points.add(Offset(xMid, yMid))

    // 第一项
    if (polyLinePoints.start == null) {
        val yEnd = getY(polyLinePoints.end!!, diff, lowestLinePoint)
        path.moveTo(xMid, yMid)
        path.lineTo(xEnd, yEnd)
        path.lineTo(xEnd, size.height)
        path.lineTo(xMid, size.height)
        drawLine(
            color = lineColor,
            start = Offset(xMid, yMid),
            strokeWidth = lineStrokeWidth,
            end = Offset(xEnd, yEnd)
        )
    } else {
        val y0 = getY(polyLinePoints.start, diff, lowestLinePoint)
        if (polyLinePoints.end == null) {
            path.moveTo(x0, y0)
            path.lineTo(xMid, yMid)
            path.lineTo(xMid, size.height)
            path.lineTo(x0, size.height)

            // 最后一项
            drawLine(
                color = lineColor,
                start = Offset(x0, y0),
                strokeWidth = lineStrokeWidth,
                end = Offset(xMid, yMid)
            )
        } else {
            val yEnd = getY(polyLinePoints.end, diff, lowestLinePoint)
            path.moveTo(x0, y0)
            path.lineTo(xMid, yMid)
            path.lineTo(xEnd, yEnd)
            path.lineTo(xEnd, size.height)
            path.lineTo(x0, size.height)
            // 中间所有项，分两段画出
            drawLine(
                color = lineColor,
                start = Offset(x0, y0),
                strokeWidth = lineStrokeWidth,
                end = Offset(xMid, yMid),
            )
            drawLine(
                color = lineColor,
                start = Offset(xMid, yMid),
                strokeWidth = lineStrokeWidth,
                end = Offset(xEnd, yEnd)
            )
        }
    }
    path.close()

    if (isHighLine) {
        // 竖直线
        drawLine(
            color = Color.LightGray,
            start = Offset(xMid, 0f),
            end = Offset(xMid, size.height),
            strokeWidth = 2f
        )
    }


    // 在圆点上显示气温
    drawCustomText(
        text = polyLinePoints.mid.roundToInt().toString() + "℃",
        x = xMid,
        y = if (isHighLine) yMid - pointStrokeWidth * 4 - 5 else yMid + pointStrokeWidth * 2,
        lineTextColor = lineTextColor
    )

    if (histogramPoint > 0) {
        val histogramDiff = highestHistogramPoint - lowestHistogramPoint
        val histogramHeight = size.height / histogramDiff * (histogramPoint - lowestHistogramPoint)
        drawHistogram(histogramHeight, xMid)
    }

    //绘制路径，即曲线下方的渐变色
    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = arrayListOf(Color(0x96d6dbbee), Color(0x00000000))
        )
    )

    //绘制蓝色圆点
    drawPoints(
        pointMode = PointMode.Points,
        color = Color(0xFF357AFF),
        strokeWidth = pointStrokeWidth * 2,
        points = points,
        cap = StrokeCap.Round,
    )

    //绘制白色圆点
    drawPoints(
        pointMode = PointMode.Points,
        color = Color.White,
        strokeWidth = pointStrokeWidth,
        points = points,
        cap = StrokeCap.Round,
    )
}

fun DrawScope.getY(y: Float, diff: Float, minY: Float): Float {
    return size.height - (size.height / (diff/* + 2*/) * ((y - minY)/* + 1*/))
}

fun DrawScope.drawHistogram(histogramHeight: Float, x: Float) {
    // 小时降水量柱状图
    drawRoundRect(
        Color.Green.copy(alpha = 0.3f),
        topLeft = Offset(
            x - 8.dp.toPx() / 2,
            size.height - histogramHeight
        ),
        size = Size(8.dp.toPx(), histogramHeight),
        cornerRadius = CornerRadius(8f, 8f)
    )
}

fun DrawScope.drawCustomText(
    text: String,
    x: Float,
    y: Float,
    lineTextColor: Color
) {
    drawIntoCanvas {
        val painter = Paint().apply {
            style = Paint.Style.FILL
            color = lineTextColor.toArgb()
            textSize = lineChartTextFontSizeDp.sp.toPx()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            setShadowLayer(2f, 0f, 1f, android.graphics.Color.GRAY)
        }
        val fontMetrics: Paint.FontMetrics = painter.fontMetrics
        it.nativeCanvas.drawText(
            text,
            x,
            y - fontMetrics.top,
            painter
        )
    }
}
