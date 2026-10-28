package com.example.unisalud_app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unisalud_app.model.ScheduleItem
import com.example.unisalud_app.model.UniSaludRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScheduleSetupScreen(
    onScheduleConfirmed: () -> Unit
) {
    var hasUploaded by remember { mutableStateOf(false) }

    val defaultParsedSchedule = listOf(
        ScheduleItem("Lunes", "08:00", "10:00", "Cálculo Diferencial", "Dr. Roberto Gómez", "roberto.gomez@tuuniversidad.edu.co", "Auditorio 301"),
        ScheduleItem("Martes", "10:15", "12:00", "Física Mecánica", "Dra. María Pérez", "maria.perez@tuuniversidad.edu.co", "Lab Física 102"),
        ScheduleItem("Miércoles", "14:00", "16:00", "Programación Orientada a Objetos", "Ing. Carlos Ruiz", "carlos.ruiz@tuuniversidad.edu.co", "Sistemas 204"),
        ScheduleItem("Jueves", "08:00", "10:00", "Álgebra Lineal", "Dr. Roberto Gómez", "roberto.gomez@tuuniversidad.edu.co", "Salón 402"),
        ScheduleItem("Viernes", "10:00", "12:00", "Bases de Datos", "Dra. Ana Torres", "ana.torres@tuuniversidad.edu.co", "Sistemas 105")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración Inicial - Horario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            UniSaludRepository.setUploadedSchedule(defaultParsedSchedule)
                            onScheduleConfirmed()
                        },
                        enabled = hasUploaded,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar y Continuar al Dashboard", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Carga tu Horario de Clases",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sube una foto o captura de pantalla de tu horario institucional. Nuestra IA multimodal extraerá automáticamente tus materias y profesores para validar tus incapacidades correctamente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedCard(
                    onClick = { hasUploaded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (hasUploaded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (hasUploaded) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = if (hasUploaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (hasUploaded) "Horario cargado con éxito (AI Procesado)" else "Tocar para subir foto de horario o PDF",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (hasUploaded) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Archivo: Horario_2025_1_Ingenieria.png",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (hasUploaded) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vista Previa de Materias Detectadas por IA:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(defaultParsedSchedule) { schedule ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = schedule.subjectName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${schedule.dayOfWeek} • ${schedule.startTime} - ${schedule.endTime} (${schedule.room})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Prof: ${schedule.professorName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
