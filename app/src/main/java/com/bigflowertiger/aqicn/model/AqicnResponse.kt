package com.bigflowertiger.aqicn.model

import java.util.*

data class AqicnResponse(val status: String, val data: AqicnData?) {
    data class AqicnData(
        val aqi: Int,
        val idx: Int, //Unique ID for the city monitoring station.
        val attributions: List<AqicnAttribution>,
        val city: AqicnCity,
        val dominentpol: String,
        val iaqi: AqicnIaqi,
        val time: AqicnTime,
        val forecast: AqicnForecast,
        val debug: AqicnDebug
    ) {
        data class AqicnAttribution(val url: String, val name: String)
        data class AqicnCity(
            val geo: List<Float>,
            val name: String,
            val url: String,
            val location: String
        )

        data class AqicnIaqi(
            val co: AqicnIaqiV,
            val dew: AqicnIaqiV,
            val h: AqicnIaqiV,
            val no2: AqicnIaqiV,
            val o3: AqicnIaqiV,
            val p: AqicnIaqiV,
            val pm10: AqicnIaqiV,
            val pm25: AqicnIaqiV,
            val r: AqicnIaqiV,
            val so2: AqicnIaqiV,
            val t: AqicnIaqiV,
            val w: AqicnIaqiV
        ) {
            data class AqicnIaqiV(val v: Float)
        }

        data class AqicnTime(
            val s: String, //Local measurement time time.
            val tz: String, //Station timezone.
            val v: Int,
            val iso: Date
        )

        data class AqicnForecast(val daily: AqicnForecastDaily) {
            data class AqicnForecastDaily(
                val o3: List<DailyIaqiValue>, //Ozone forecast
                val pm10: List<DailyIaqiValue>,
                val pm25: List<DailyIaqiValue>,
                val uvi: List<DailyIaqiValue>, //Ultra Violet Index forecast

            ) {
                data class DailyIaqiValue(
                    val avg: Int,
                    val day: Date,
                    val max: Int,
                    val min: Int
                )
            }
        }

        data class AqicnDebug(val sync: Date)
    }
}
