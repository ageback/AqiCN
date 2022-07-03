package com.bigflowertiger.aqicn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bigflowertiger.aqicn.model.AqicnResponse
import com.bigflowertiger.aqicn.ui.theme.AqiCNTheme
import com.bigflowertiger.composecharts.HistogramChart
import com.bigflowertiger.composecharts.model.HistogramChartDataList
import com.bigflowertiger.composecharts.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AqicnViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.aqiState.value
            val dailyAirChartDataList =
                state.data?.data?.let { buildDailyAirQualityChartData(it.forecast) }
                    ?.let {
                        HistogramChartDataList(
                            it
                        )
                    }
            AqiCNTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()
                    ) {
                        Button(onClick = {
                            viewModel.fetchAqi("120.173580", "31.384260")
                        }) {
                            Text(text = "Click")
                        }
                        Log.d("dddddddd", dailyAirChartDataList.toString())
                        if (state.errMsg.isEmpty()) {

                            Card(
                                modifier = Modifier
                                    .padding(
                                        start = 8.dp,
                                        top = 8.dp,
                                        end = 8.dp
                                    ),
                                elevation = 4.dp
                            ) {
                                dailyAirChartDataList?.let {
                                    HistogramChart(
                                        modifier = Modifier.height(220.dp),
                                        histogramChartDataList = it,
                                        onHistogramClick = {},
                                        bgWidth = 46.dp,
                                        histogramWidth = 12.dp
                                    )
                                }
                            }
                        } else {
                            Text(text = state.errMsg)
                        }
                    }
                }
            }
        }
    }
}

fun buildDailyAirQualityChartData(dailyForecast: AqicnResponse.AqicnData.AqicnForecast):
        MutableList<HistogramChartDataList.HistogramChartData> {
    val histogramChartDataList = mutableListOf<HistogramChartDataList.HistogramChartData>()
    dailyForecast.daily.pm25.forEachIndexed { index, aqi ->
        val dayOfWeek = SunnyWeatherDateUtils.getDayOfWeek(aqi.day)
        val chartData = HistogramChartDataList.HistogramChartData(
            high = aqi.max.toFloat(),
            low = aqi.min.toFloat(),
            highest = dailyForecast.daily.pm25.maxOf { it.max }.toFloat(),
            lowest = dailyForecast.daily.pm25.maxOf { it.min }.toFloat(),
            highTitle = aqi.max.toString(),
            lowTitle = aqi.min.toString(),
            histogramColor = listOf(
                getAQILevel(aqi.max.toFloat()).color,
                getAQILevel(aqi.min.toFloat()).color
            ),
            title = SunnyWeatherDateUtils.getDayOfDate(aqi.day),
            titleColor = getChartTitleColor(dayOfWeek),
            titleFontWeight = getChartTitleFontWeight(dayOfWeek),
            subtitle = SunnyWeatherDateUtils.getShortDate(aqi.day)
        )
        histogramChartDataList.add(chartData)
    }
    return histogramChartDataList
}

private fun getChartTitleFontWeight(dayOfWeek: Int) =
    if (dayOfWeek == 6 || dayOfWeek == 0) FontWeight.Bold else FontWeight.Normal

private fun getChartTitleColor(dayOfWeek: Int) = when (dayOfWeek) {
    6 -> androidx.compose.ui.graphics.Color.Magenta
    0 -> androidx.compose.ui.graphics.Color.Red
    else -> androidx.compose.ui.graphics.Color.Unspecified
}

/**
 * AQI 等级
 */
fun getAQILevel(value: Float): AQIDescription =
    when (value) {
        in 0f..50f -> AQIDescription("一级", "优", Green9966)
        in 51f..100f -> AQIDescription("二级", "良", YellowFF33)
        in 101f..150f -> AQIDescription("三级", "轻度污染", OrangeFF)
        in 151f..200f -> AQIDescription("四级", "中度污染", RedFF)
        in 201f..300f -> AQIDescription("五级", "重度污染", Purple6699)
        else -> AQIDescription("六级", "严重污染", Red800)
    }

data class AQIDescription(
    val level: String,
    val quality: String,
    val color: androidx.compose.ui.graphics.Color
)