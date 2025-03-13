package com.mhss.app.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhss.app.domain.model.CanvasDrawing
import com.mhss.app.domain.model.DrawPath
import com.mhss.app.domain.repository.CanvasRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.*

@KoinViewModel
class CanvasDetailViewModel(
    private val repository: CanvasRepository
) : ViewModel() {

    var drawing by mutableStateOf<CanvasDrawing?>(null)
        private set

    fun loadDrawing(id: String) {
        viewModelScope.launch {
            drawing = repository.getDrawing(id)
        }
    }

    fun saveDrawing(paths: List<DrawPath>) {
        viewModelScope.launch {
            val currentDrawing = drawing
            val newDrawing = if (currentDrawing != null) {
                currentDrawing.copy(
                    paths = paths,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                CanvasDrawing(
                    id = UUID.randomUUID().toString(),
                    title = "Drawing ${System.currentTimeMillis()}",
                    paths = paths,
                    backgroundColor = android.graphics.Color.WHITE,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }
            repository.saveDrawing(newDrawing)
        }
    }
} 