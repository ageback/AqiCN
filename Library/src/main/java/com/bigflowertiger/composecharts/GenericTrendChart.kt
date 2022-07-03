package com.bigflowertiger.composecharts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bigflowertiger.composecharts.model.PolyLineChartDataList

const val dateFontSizeSp = 14
const val lineChartTextFontSizeDp = 13
const val skyconIconSizeDp = 32

// 折线图每格宽度，单位dp
const val dateBoxWidthDp = 64
const val dailyTrendSpacerHeight = 8

/*
 * 接下来是自定义温度折线图，首先我们来分析下9天的数据，那么需要9个点，也就是屏幕需要8等分，然后分别绘制线段和端点就可以了。
 * 整体关于Canvas绘制的请查看之前的文章，这里我们需要注意一点就是：绘制的端点是有半径的，我们绘制区域的时候，
 * x轴前后需要留出来这个半径能把首尾的端点全部展示出来，否则首尾的端点只能显示半个。
————————————————
版权声明：本文为CSDN博主「乐翁龙」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u010976213/article/details/117284079
 */



@Composable
fun GenericTrendChart(
    modifier: Modifier = Modifier,
    polyLineChartDataList: PolyLineChartDataList,
    skyIconType: SkyIconType,
    showLowLine: Boolean,
    gridWidth: Int = dateBoxWidthDp,
    lazyListState: LazyListState = rememberLazyListState(),
    onDailyClick: (Int) -> Unit
) {
    val lineColorHigh = colorResource(id = R.color.lightPrimary_3)
    val lineColorLow = colorResource(id = R.color.teal_700)
    val lineTextColor = MaterialTheme.colors.onSurface//.copy(alpha = 0.5f)

    LazyRow(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 8.dp, bottom = 8.dp),
        state = lazyListState
    ) {
        itemsIndexed(polyLineChartDataList.data) { index, chartData ->
//            Text(text = "item $index  ")

            ConstraintLayout(modifier = modifier.width(IntrinsicSize.Max)) {
                val (topPanel, chart, chartBg, histogramTitle, bottomPanel) = createRefs()
                // 日期、
                // 日间天气和图标
                // 顶部每天的日期和图标
                Box(
                    modifier = Modifier
                        .constrainAs(topPanel) {
                            top.linkTo(parent.top)
                        }
                        .wrapContentHeight()
                        .width(gridWidth.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = chartData.title,
                            color = chartData.titleColor,
                            fontWeight = chartData.titleFontWeight
                        )
                        Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                        Text(
                            text = chartData.subtitle,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                        SkyIcon(
                            iconType = skyIconType,
                            iconId = chartData.topIcon
                        )
                        Spacer(modifier = Modifier.height(dailyTrendSpacerHeight.dp))
                        Text(
                            text = chartData.topIconTitle,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height((dailyTrendSpacerHeight * 2).dp))
                    }
                }

                // 中间折线图
                Canvas(modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .constrainAs(chart) {
                        top.linkTo(topPanel.bottom)
                    }) {
                    drawCharts(
                        polyLinePoints = chartData.highLinePointsList,
                        lineColor = lineColorHigh,
                        lineTextColor = lineTextColor,
                        lowestLinePoint = chartData.lowestLinePoint,
                        highestLinePoint = chartData.highestLinePoint,
                        histogramPoint = chartData.histogramHighPoint,
                        highestHistogramPoint = chartData.highestHistogramPoint,
                        gridWidth = gridWidth,
                        lowestHistogramPoint = chartData.lowestHistogramPoint
                    )
                    if (showLowLine) {
                        drawCharts(
                            polyLinePoints = chartData.lowLinePointsList,
                            isHighLine = false,
                            lineColor = lineColorLow,
                            lineTextColor = lineTextColor,
                            lowestLinePoint = chartData.lowestLinePoint,
                            gridWidth = gridWidth,
                            highestLinePoint = chartData.highestLinePoint
                        )
                    }
                }

                // 底部柱状图对应的标题
                Row(
                    modifier = Modifier
                        .constrainAs(histogramTitle) {
                            top.linkTo(chart.bottom)
                        }
                        .padding(top = if (showLowLine) 4.dp else 24.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .alpha(0.6f)
                            .align(Alignment.Bottom)
                            .width(gridWidth.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = chartData.histogramTitle ?: "",
                                fontSize = dateFontSizeSp.sp
                            )
                        }
                    }
                }

                if (showLowLine) {
                    // 底部夜间天气和图标，在折线图下方
                    Row(
                        modifier = Modifier
                            .constrainAs(bottomPanel) {
                                top.linkTo(histogramTitle.bottom)
                            }
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(gridWidth.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = chartData.bottomIconTitle ?: "",
                                    fontSize = dateFontSizeSp.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                SkyIcon(
                                    iconType = skyIconType,
                                    iconId = chartData.bottomIcon!!,
                                )
                            }
                        }
                    }
                }

                BgBox(
                    modifier = Modifier
                        .constrainAs(chartBg) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    index = index,
                    onEachBoxClick = onDailyClick,
                    bgWidth = gridWidth.dp
                )
            }
        }
    }
}

@Composable
fun BgBox(
    modifier: Modifier = Modifier,
    index: Int,
    onEachBoxClick: (Int) -> Unit,
    bgWidth: Dp
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable {
                    onEachBoxClick(index)
                }
                .width(bgWidth),
            contentAlignment = Alignment.TopCenter
        ) { }
    }
}


