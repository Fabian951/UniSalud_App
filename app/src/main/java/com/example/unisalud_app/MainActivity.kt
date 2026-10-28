package com.example.unisalud_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.unisalud_app.view.*
import com.example.unisalud_app.viewmodel.Screen
import com.example.unisalud_app.viewmodel.UniSaludViewModel
import com.example.unisalud_app.ui.theme.UniSalud_AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: UniSaludViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniSalud_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val screen = viewModel.currentScreen) {
                        is Screen.Login -> {
                            LoginScreen(
                                onLoginSuccess = { role, email ->
                                    viewModel.login(role, email)
                                }
                            )
                        }
                        is Screen.StudentSetup -> {
                            StudentScheduleSetupScreen(
                                onScheduleConfirmed = {
                                    viewModel.confirmScheduleSetup()
                                }
                            )
                        }
                        is Screen.StudentDashboard -> {
                            StudentDashboardScreen(
                                studentEmail = viewModel.currentUserEmail.ifEmpty { "estudiante@tuuniversidad.edu.co" },
                                onNavigateToUpload = {
                                    viewModel.navigateToUploadIncapacidad()
                                },
                                onLogout = {
                                    viewModel.logout()
                                }
                            )
                        }
                        is Screen.IncapacidadForm -> {
                            IncapacidadFormScreen(
                                onSubmitted = { start, end, photo ->
                                    viewModel.submitIncapacidad(start, end, photo)
                                },
                                onBack = {
                                    viewModel.navigateBackToStudentDashboard()
                                }
                            )
                        }
                        is Screen.DoctorDashboard -> {
                            DoctorDashboardScreen(
                                doctorEmail = viewModel.currentUserEmail.ifEmpty { "medico.salud@tuuniversidad.edu.co" },
                                onSelectIncapacidad = { inc ->
                                    viewModel.selectIncapacidadForDoctor(inc)
                                },
                                onLogout = {
                                    viewModel.logout()
                                }
                            )
                        }
                        is Screen.DoctorValidation -> {
                            DoctorValidationScreen(
                                incapacidad = screen.incapacidad,
                                onStatusUpdated = { newStatus, obs ->
                                    viewModel.updateIncapacidadStatus(screen.incapacidad.id, newStatus, obs)
                                },
                                onBack = {
                                    viewModel.logout() // or back to dashboard
                                    viewModel.login(com.example.unisalud_app.model.UserRole.DOCTOR, viewModel.currentUserEmail.ifEmpty { "medico.salud@tuuniversidad.edu.co" })
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
