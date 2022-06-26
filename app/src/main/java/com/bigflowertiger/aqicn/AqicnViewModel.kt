package com.bigflowertiger.aqicn

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigflowertiger.aqicn.domain.AqicnRepository
import com.bigflowertiger.aqicn.model.AqicnResponse
import com.bigflowertiger.aqicn.model.AqicnState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AqicnViewModel @Inject constructor(private val repository: AqicnRepository) : ViewModel() {
    private val _aqiState: MutableState<AqicnState> = mutableStateOf(AqicnState("", null, false))
    val aqiState: State<AqicnState> = _aqiState

    init {
//        fetchAqi("120.173580", "31.384260")
    }

    fun fetchAqi(lng: String, lat: String) {
        viewModelScope.launch {
            try {
                _aqiState.value =
                    AqicnState(data = repository.getGeolocalizedFeed(lng, lat), loading = false)
                Log.d("DDD", _aqiState.value.toString())
            } catch (ex: Exception) {
                _aqiState.value =
                    AqicnState(
                        ex.message ?: "未知异常",
                        null,
                        loading = false
                    )
            }
        }
    }
}