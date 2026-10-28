package com.example.unisalud_app.model

data class IncapacidadItem(
    val id: String,
    val studentName: String,
    val studentEmail: String,
    val studentCode: String,
    val startDate: String,
    val endDate: String,
    val status: String, // "Pendiente", "Aprobada", "Rechazada"
    val photoPlaceholder: String,
    val observations: String = "",
    val submissionDate: String,
    val scheduleItems: List<ScheduleItem>
)
