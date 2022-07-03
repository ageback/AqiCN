package com.bigflowertiger.composecharts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bigflowertiger.composecharts.config.weatherFontFamily
import com.bigflowertiger.composecharts.model.HistogramChartDataList


@Composable
fun HistogramChart(
    modifier: Modifier = Modifier,
    histogramChartDataList: HistogramChartDataList,
    onHistogramClick: (Int) -> Unit,
    bgWidth: Dp = dateBoxWidthDp.dp,
    histogramWidth: Dp = 8.dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentSize()
            .padding(top = 8.dp, bottom = 8.dp),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(0.dp)
    ) {
        itemsIndexed(histogramChartDataList.data) { index, item ->
            ConstraintLayout(modifier = modifier.width(IntrinsicSize.Max))
            {
                val (topPanel, histogramChart, bottomPanel, bgBox) = createRefs()
                Row(
                    modifier = Modifier.constrainAs(topPanel) {
                        top.linkTo(parent.top)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(Alignment.Bottom)
                            .width(bgWidth),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        // 顶部每天的日期
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.title,
                                color = item.titleColor,
                                fontSize = item.titleFontSize,
                                fontWeight = item.titleFontWeight
                            )
                            Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                            if (item.subtitle.isNotEmpty()) {
                                Text(
                                    text = item.subtitle,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                            }
                            if (item.iconId > 0) {
                                Text(
                                    text = stringResource(id = item.iconId),
                                    fontSize = 32.sp,
                                    fontFamily = weatherFontFamily
                                )
                                Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                            }

                            if (item.highTitle.isNotEmpty()) {
                                Text(
                                    text = item.highTitle,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // 柱状图
                Box(modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(histogramChart) {
                        top.linkTo(topPanel.bottom)
                    }) {
                    Canvas(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth()

                    ) {
                        val startX = (bgWidth.roundToPx()) / 2

                        val diff = item.highest - item.lowest

                        val delta = item.high - item.lowest
                        val histogramHeight = if (delta == 0f) 0f else size.height / diff * delta

                        val y =
                            if (delta == 0f) 0f else size.height - size.height / diff * delta
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                item.histogramColor,
                                startY = y,
                                endY = histogramHeight
                            ),
//                            color = item.histogramColor,
                            topLeft = Offset(
                                startX - histogramWidth.toPx() / 2,
                                y
                            ),
                            size = Size(histogramWidth.toPx(), histogramHeight),
                            cornerRadius = CornerRadius(4f, 4f)
                        )

                        // 垂直线
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(startX.toFloat(), 0f),
                            end = Offset(startX.toFloat(), size.height),
                            strokeWidth = 1f
                        )
                    }

                }

                // 底部
                Row(
                    modifier = Modifier
                        .constrainAs(bottomPanel) {
                            top.linkTo(histogramChart.bottom)
                        }
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(bgWidth),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item.lowTitle,
                                fontSize = dateFontSizeSp.sp
                            )
                        }
                    }
                }

                BgBox(
                    modifier = Modifier
                        .constrainAs(bgBox) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    index = index,
                    onEachBoxClick = onHistogramClick,
                    bgWidth = bgWidth
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun HistogramChartPreview() {
//    val daily = getPreviewWeatherGeneral().general.daily
//    HistogramChart(
//        highList = daily.temperature.map { it.max },
//        lowList = daily.temperature.map { it.min })
//}
