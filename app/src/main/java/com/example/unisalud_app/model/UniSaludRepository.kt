package com.example.unisalud_app.model

import androidx.compose.runtime.mutableStateListOf

object UniSaludRepository {
    // Schedule starts empty until the student uploads their schedule
    val sampleSchedule = mutableStateListOf<ScheduleItem>()

    // Incapacidades starts empty (no pre-created data)
    val incapacidades = mutableStateListOf<IncapacidadItem>()

    fun setUploadedSchedule(items: List<ScheduleItem>) {
        sampleSchedule.clear()
        sampleSchedule.addAll(items)
    }

    fun addIncapacidad(item: IncapacidadItem) {
        incapacidades.add(0, item)
    }

    fun updateIncapacidadStatus(id: String, newStatus: String, observations: String) {
        val index = incapacidades.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = incapacidades[index]
            incapacidades[index] = old.copy(status = newStatus, observations = observations)
        }
    }
}
