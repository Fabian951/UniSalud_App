package com.example.unisalud_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unisalud_app.model.IncapacidadItem
import com.example.unisalud_app.model.UniSaludRepository
import com.example.unisalud_app.model.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Screen {
    object Login : Screen()
    object StudentSetup : Screen()
    object StudentDashboard : Screen()
    object IncapacidadForm : Screen()
    object DoctorDashboard : Screen()
    data class DoctorValidation(val incapacidad: IncapacidadItem) : Screen()
}

class UniSaludViewModel : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Login)
        private set

    var currentUserEmail by mutableStateOf("")
        private set

    var currentUserRole by mutableStateOf(UserRole.STUDENT)
        private set

    var doctorSearchQuery by mutableStateOf("")

    val schedule = UniSaludRepository.sampleSchedule
    val incapacidades = UniSaludRepository.incapacidades

    fun login(role: UserRole, email: String) {
        currentUserRole = role
        currentUserEmail = email
        if (role == UserRole.STUDENT) {
            currentScreen = Screen.StudentSetup
        } else {
            currentScreen = Screen.DoctorDashboard
        }
    }

    fun confirmScheduleSetup() {
        currentScreen = Screen.StudentDashboard
    }

    fun navigateToUploadIncapacidad() {
        currentScreen = Screen.IncapacidadForm
    }

    fun navigateBackToStudentDashboard() {
        currentScreen = Screen.StudentDashboard
    }

    fun submitIncapacidad(startDate: String, endDate: String, photo: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        
        val rawName = if (currentUserEmail.isNotEmpty()) currentUserEmail.substringBefore("@").replace('.', ' ') else "Estudiante UniSalud"
        val formattedName = rawName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        val newInc = IncapacidadItem(
            id = "INC-00${incapacidades.size + 1}",
            studentName = formattedName,
            studentEmail = currentUserEmail.ifEmpty { "estudiante@tuuniversidad.edu.co" },
            studentCode = "202310892",
            startDate = startDate,
            endDate = endDate,
            status = "Pendiente",
            photoPlaceholder = photo,
            submissionDate = currentDate,
            scheduleItems = schedule.take(2)
        )
        UniSaludRepository.addIncapacidad(newInc)
        currentScreen = Screen.StudentDashboard
    }

    fun selectIncapacidadForDoctor(item: IncapacidadItem) {
        currentScreen = Screen.DoctorValidation(item)
    }

    fun updateIncapacidadStatus(id: String, status: String, observations: String) {
        UniSaludRepository.updateIncapacidadStatus(id, status, observations)
        currentScreen = Screen.DoctorDashboard
    }

    fun logout() {
        currentUserEmail = ""
        currentScreen = Screen.Login
    }
}
