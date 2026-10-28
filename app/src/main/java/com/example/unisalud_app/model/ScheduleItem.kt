package com.example.unisalud_app.model

data class ScheduleItem(
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val subjectName: String,
    val professorName: String,
    val professorEmail: String,
    val room: String
)
